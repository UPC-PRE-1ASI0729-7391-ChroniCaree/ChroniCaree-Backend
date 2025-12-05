package com.chronicare.platform.invitations.application.internal.commandservices;

import com.chronicare.platform.doctors.domain.model.commands.CreateDoctorCommand;
import com.chronicare.platform.doctors.domain.services.DoctorCommandService;
import com.chronicare.platform.doctors.infrastructure.persistence.jpa.repositories.DoctorRepository;
import com.chronicare.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.repositories.UserRepository;
import com.chronicare.platform.iam.domain.model.valueobjects.Roles;
import com.chronicare.platform.iam.infrastructure.tokens.RefreshTokenService;
import com.chronicare.platform.invitations.domain.model.aggregates.Invitation;
import com.chronicare.platform.invitations.domain.model.commands.AcceptInvitationCommand;
import com.chronicare.platform.invitations.domain.model.commands.CreateInvitationCommand;
import com.chronicare.platform.invitations.domain.model.commands.DeleteInvitationCommand;
import com.chronicare.platform.invitations.domain.model.commands.UpdateInvitationStatusCommand;
import com.chronicare.platform.invitations.domain.model.valueobjects.InvitationStatus;
import com.chronicare.platform.invitations.domain.services.InvitationCommandService;
import com.chronicare.platform.invitations.infrastructure.persistence.jpa.repositories.InvitationRepository;
import com.chronicare.platform.invitations.interfaces.rest.resources.AcceptInvitationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of Invitation Command Service
 */
@Service
public class InvitationCommandServiceImpl implements InvitationCommandService {

    private static final Logger log = LoggerFactory.getLogger(InvitationCommandServiceImpl.class);

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorCommandService doctorCommandService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public InvitationCommandServiceImpl(
            InvitationRepository invitationRepository,
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            DoctorCommandService doctorCommandService,
            TokenService tokenService,
            RefreshTokenService refreshTokenService,
            PasswordEncoder passwordEncoder
    ) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.doctorCommandService = doctorCommandService;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Optional<Invitation> handle(CreateInvitationCommand command) {
        log.info("Creating invitation for email: {} in tenant: {}", command.email(), command.tenantId());

        // Check if there's already a pending invitation for this email in this tenant
        boolean exists = invitationRepository.existsByEmailAndTenantIdAndStatus(
                command.email(), command.tenantId(), InvitationStatus.PENDING);
        
        if (exists) {
            log.warn("Pending invitation already exists for email: {} in tenant: {}", 
                    command.email(), command.tenantId());
            throw new IllegalStateException("A pending invitation already exists for this email");
        }

        // Check if user already exists AND is already a doctor in this tenant
        var existingUser = userRepository.findByEmail_Address(command.email());
        if (existingUser.isPresent() && command.role().equalsIgnoreCase("doctor")) {
            // Check if there's already a doctor for this user in this tenant
            var existingDoctor = doctorRepository.findByUserId(existingUser.get().getId());
            if (existingDoctor.isPresent() && existingDoctor.get().getTenantId().equals(command.tenantId())) {
                log.warn("User already has a doctor profile in tenant: {}", command.tenantId());
                throw new IllegalStateException("This user already has a doctor profile in this hospital");
            }
            log.info("User exists but not as doctor in tenant {}. Invitation can proceed.", command.tenantId());
        }

        Invitation invitation = new Invitation(
                command.tenantId(),
                command.invitedBy(),
                command.email(),
                command.role(),
                command.expiresInDays()
        );

        Invitation saved = invitationRepository.save(invitation);
        log.info("Invitation created with ID: {} and token: {}", saved.getId(), saved.getToken());
        
        return Optional.of(saved);
    }

    @Override
    @Transactional
    public Optional<AcceptInvitationResponse> handle(AcceptInvitationCommand command) {
        log.info("Accepting invitation: {}", command.invitationId());

        // Find invitation
        Invitation invitation = invitationRepository.findById(command.invitationId())
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        // Validate token
        if (!invitation.getToken().equals(command.token())) {
            log.warn("Invalid token for invitation: {}", command.invitationId());
            throw new IllegalArgumentException("Invalid invitation token");
        }

        // Check if invitation is valid
        if (!invitation.isValid()) {
            log.warn("Invitation is not valid: {} (status: {}, expired: {})", 
                    command.invitationId(), invitation.getStatus(), invitation.isExpired());
            throw new IllegalStateException("Invitation is not valid or has expired");
        }

        // Check if user already exists with this email
        User user;
        var existingUser = userRepository.findByEmail_Address(invitation.getEmail());
        
        if (existingUser.isPresent()) {
            // User exists - use existing user
            user = existingUser.get();
            log.info("Using existing user with ID: {} for invitation (role: {})", user.getId(), user.getRole());
        } else {
            // Create new user
            String fullName = command.firstName() + " " + command.lastName();
            user = new User(
                    invitation.getEmail(),
                    passwordEncoder.encode(command.password()),
                    fullName,
                    Roles.DOCTOR,
                    invitation.getTenantId()
            );
            user = userRepository.save(user);
            log.info("Created new user with ID: {}", user.getId());
        }

        // Create doctor
        CreateDoctorCommand createDoctorCommand = new CreateDoctorCommand(
                user.getId(),
                invitation.getTenantId(),
                command.firstName(),
                command.lastName(),
                command.dni(),
                command.specialty(),
                command.licenseNumber(),
                command.phone(),
                null, // consultationFee
                List.of("Español"), // default language
                List.of() // education
        );
        
        var doctorOpt = doctorCommandService.handle(createDoctorCommand);
        if (doctorOpt.isEmpty()) {
            log.error("Failed to create doctor for user: {}", user.getId());
            throw new IllegalStateException("Failed to create doctor");
        }
        var doctor = doctorOpt.get();
        log.info("Doctor created with ID: {}", doctor.getId());

        // Accept invitation
        invitation.accept();
        invitationRepository.save(invitation);

        // Generate tokens
        String accessToken = tokenService.generateToken(user.getEmailAddress(), user.getRole().getName());
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        // Build response
        var userResponse = new AcceptInvitationResponse.UserResponse(
                user.getId(),
                user.getEmailAddress(),
                user.getRole().getName(),
                user.getName()
        );

        var doctorResponse = new AcceptInvitationResponse.DoctorResponse(
                doctor.getId(),
                doctor.getUserId(),
                doctor.getTenantId(),
                doctor.getName().getFirstName(),
                doctor.getName().getLastName(),
                doctor.getSpecialty().getValue(),
                doctor.getLicenseNumber().getValue()
        );

        return Optional.of(new AcceptInvitationResponse(
                true,
                userResponse,
                doctorResponse,
                accessToken,
                refreshToken.getToken()
        ));
    }

    @Override
    @Transactional
    public Optional<Invitation> handle(UpdateInvitationStatusCommand command) {
        log.info("Updating invitation status: {} to {}", command.invitationId(), command.status());

        Invitation invitation = invitationRepository.findById(command.invitationId())
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        InvitationStatus newStatus = parseStatus(command.status());
        
        switch (newStatus) {
            case ACCEPTED -> invitation.accept();
            case REJECTED -> invitation.reject();
            case EXPIRED -> invitation.cancel();
            default -> throw new IllegalArgumentException("Cannot change status to: " + command.status());
        }

        return Optional.of(invitationRepository.save(invitation));
    }

    @Override
    @Transactional
    public void handle(DeleteInvitationCommand command) {
        log.info("Deleting invitation: {}", command.invitationId());
        
        if (!invitationRepository.existsById(command.invitationId())) {
            throw new IllegalArgumentException("Invitation not found");
        }
        
        invitationRepository.deleteById(command.invitationId());
    }

    private InvitationStatus parseStatus(String status) {
        return switch (status.toLowerCase()) {
            case "pending" -> InvitationStatus.PENDING;
            case "accepted" -> InvitationStatus.ACCEPTED;
            case "rejected" -> InvitationStatus.REJECTED;
            case "expired" -> InvitationStatus.EXPIRED;
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        };
    }
}
