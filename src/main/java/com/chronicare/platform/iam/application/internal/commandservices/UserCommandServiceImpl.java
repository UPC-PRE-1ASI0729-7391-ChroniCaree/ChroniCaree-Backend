package com.chronicare.platform.iam.application.internal.commandservices;

import com.chronicare.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.chronicare.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.chronicare.platform.iam.infrastructure.tokens.RefreshTokenService;
import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.commands.RegisterHospitalAdminCommand;
import com.chronicare.platform.iam.domain.model.commands.RegisterUserCommand;
import com.chronicare.platform.iam.domain.model.commands.SignInCommand;
import com.chronicare.platform.iam.domain.model.commands.UpdateUserCommand;
import com.chronicare.platform.iam.domain.model.repositories.UserRepository;
import com.chronicare.platform.iam.domain.model.valueobjects.Roles;
import com.chronicare.platform.iam.domain.services.UserCommandService;
import com.chronicare.platform.tenants.domain.aggregates.Tenant;
import com.chronicare.platform.tenants.domain.repository.TenantRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * User Command Service Implementation
 * @summary Handles user commands
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {
    private static final Logger logger = Logger.getLogger(UserCommandServiceImpl.class.getName());
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final TenantRepository tenantRepository;

    public UserCommandServiceImpl(
            UserRepository userRepository, 
            HashingService hashingService, 
            TokenService tokenService, 
            RefreshTokenService refreshTokenService,
            TenantRepository tenantRepository) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.tenantRepository = tenantRepository;
    }

    @Override
    @Transactional
    public Optional<User> handle(RegisterUserCommand command) {
        logger.info("Handling RegisterUserCommand for email: " + command.email());
        // Check if email already exists
        if (userRepository.existsByEmail_Address(command.email())) {
            logger.warning("Email already exists: " + command.email());
            throw new IllegalArgumentException("Email already exists: " + command.email());
        }

        var user = new User(
                command.email(),
                hashingService.encode(command.password()),
                command.name(),
                command.role(),
                command.tenantId()
        );
        var savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: " + savedUser.getId());
        return Optional.of(savedUser);
    }

    @Override
    @Transactional
    public Optional<ImmutablePair<User, Long>> handle(RegisterHospitalAdminCommand command) {
        logger.info("========== UserCommandServiceImpl: RegisterHospitalAdminCommand START ==========");
        logger.info("Command received:");
        logger.info("  - Email: " + command.email());
        logger.info("  - Name: " + command.name());
        logger.info("  - Hospital Name: " + command.hospitalName());
        logger.info("  - Hospital Email: " + command.hospitalEmail());
        
        // Check if email already exists
        logger.info("Step 1: Checking if email exists...");
        boolean emailExists = userRepository.existsByEmail_Address(command.email());
        logger.info("  - Email exists: " + emailExists);
        if (emailExists) {
            logger.severe("❌ Email already exists: " + command.email());
            throw new IllegalArgumentException("Email already exists: " + command.email());
        }
        logger.info("✓ Email is available");

        // Check if hospital name already exists
        logger.info("Step 2: Checking if hospital name exists...");
        Optional<Tenant> existingTenant = tenantRepository.findByName(command.hospitalName());
        logger.info("  - Hospital name exists: " + existingTenant.isPresent());
        if (existingTenant.isPresent()) {
            logger.severe("❌ Hospital name already exists: " + command.hospitalName());
            throw new IllegalArgumentException("Hospital name already exists: " + command.hospitalName());
        }
        logger.info("✓ Hospital name is available");

        // 1. Create User first (without tenantId)
        logger.info("Step 3: Creating User (HOSPITAL_ADMIN)...");
        logger.info("  - Email: " + command.email());
        logger.info("  - Name: " + command.name());
        logger.info("  - Role: HOSPITAL_ADMIN");
        logger.info("  - TenantId: null (will be set later)");
        logger.info("  - Hashing password...");
        
        var user = new User(
                command.email(),
                hashingService.encode(command.password()),
                command.name(),
                Roles.HOSPITAL_ADMIN,
                null // tenantId will be set after tenant creation
        );
        
        logger.info("  - Saving user to database...");
        var savedUser = userRepository.save(user);
        logger.info("✓ User saved successfully:");
        logger.info("  - User ID: " + savedUser.getId());
        logger.info("  - Email: " + savedUser.getEmailAddress());
        logger.info("  - Role: " + savedUser.getRole().getName());
        logger.info("  - TenantId: " + savedUser.getTenantId());

        // 2. Create Tenant with adminUserId
        logger.info("Step 4: Creating Tenant (Hospital)...");
        logger.info("  - Name: " + command.hospitalName());
        logger.info("  - Admin User ID: " + savedUser.getId());
        logger.info("  - Email: " + command.hospitalEmail());
        logger.info("  - Phone: " + command.hospitalPhone());
        logger.info("  - Address: " + command.hospitalAddress());
        logger.info("  - Status: ACTIVE");
        logger.info("  - Max Doctors: 10 (default)");
        
        var tenant = Tenant.builder()
                .name(command.hospitalName())
                .adminUserId(savedUser.getId())
                .email(command.hospitalEmail())
                .phone(command.hospitalPhone())
                .address(command.hospitalAddress())
                .status("ACTIVE")
                .registrationDate(LocalDateTime.now())
                .allowIndependentDoctors(false)
                .requirePatientApproval(true)
                .maxDoctors(10) // Default value
                .build();
        
        logger.info("  - Saving tenant to database...");
        var savedTenant = tenantRepository.save(tenant);
        logger.info("✓ Tenant saved successfully:");
        logger.info("  - Tenant ID: " + savedTenant.getId());
        logger.info("  - Name: " + savedTenant.getName());
        logger.info("  - Admin User ID: " + savedTenant.getAdminUserId());
        logger.info("  - Status: " + savedTenant.getStatus());

        // 3. Update User with tenantId
        logger.info("Step 5: Updating User with tenantId...");
        logger.info("  - User ID: " + savedUser.getId());
        logger.info("  - Setting tenantId to: " + savedTenant.getId());
        
        savedUser.setTenantId(savedTenant.getId());
        logger.info("  - Saving updated user...");
        savedUser = userRepository.save(savedUser);
        
        logger.info("✓ User updated successfully:");
        logger.info("  - User ID: " + savedUser.getId());
        logger.info("  - TenantId: " + savedUser.getTenantId());
        
        logger.info("Step 6: Verifying data consistency...");
        // Verificar que el tenant realmente se guardó
        Optional<Tenant> verifyTenant = tenantRepository.findById(savedTenant.getId());
        logger.info("  - Tenant findById(" + savedTenant.getId() + "): " + verifyTenant.isPresent());
        if (verifyTenant.isPresent()) {
            logger.info("  - Verified Tenant Name: " + verifyTenant.get().getName());
            logger.info("  - Verified Admin User ID: " + verifyTenant.get().getAdminUserId());
        } else {
            logger.severe("❌ CRITICAL: Tenant NOT FOUND immediately after saving!");
        }
        
        // Verificar findByAdminUserId
        Optional<Tenant> verifyByAdmin = tenantRepository.findByAdminUserId(savedUser.getId());
        logger.info("  - Tenant findByAdminUserId(" + savedUser.getId() + "): " + verifyByAdmin.isPresent());
        if (verifyByAdmin.isPresent()) {
            logger.info("  - Verified Tenant by Admin: " + verifyByAdmin.get().getName() + " (ID: " + verifyByAdmin.get().getId() + ")");
        } else {
            logger.severe("❌ CRITICAL: Tenant NOT FOUND by adminUserId!");
        }
        
        logger.info("========== UserCommandServiceImpl: RegisterHospitalAdminCommand SUCCESS ==========");
        logger.info("✓ Final Result:");
        logger.info("  - User ID: " + savedUser.getId());
        logger.info("  - User Email: " + savedUser.getEmailAddress());
        logger.info("  - User TenantId: " + savedUser.getTenantId());
        logger.info("  - Tenant ID: " + savedTenant.getId());
        logger.info("  - Tenant Name: " + savedTenant.getName());
        logger.info("=====================================================================================");

        return Optional.of(ImmutablePair.of(savedUser, savedTenant.getId()));
    }

    @Override
    @Transactional
    public Optional<ImmutableTriple<User, String, String>> handle(SignInCommand command) {
        logger.info("Handling SignInCommand for username: " + command.username());
        var user = userRepository.findByEmail_Address(command.username());
        if (user.isEmpty()) {
            logger.warning("User not found for username: " + command.username());
            return Optional.empty();
        }
        if (!hashingService.matches(command.password(), user.get().getPassword())) {
            logger.warning("Password mismatch for username: " + command.username());
            return Optional.empty();
        }
        var token = tokenService.generateToken(user.get().getUsername(), user.get().getRole().getName());
        var refreshToken = refreshTokenService.createRefreshToken(user.get().getId());
        logger.info("User authenticated successfully: " + command.username());
        return Optional.of(ImmutableTriple.of(user.get(), token, refreshToken.getToken()));
    }

    @Override
    @Transactional
    public Optional<User> handle(UpdateUserCommand command) {
        var userOptional = userRepository.findById(command.userId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + command.userId());
        }

        var user = userOptional.get();
        user.update(command);
        var updatedUser = userRepository.save(user);
        return Optional.of(updatedUser);
    }

    @Override
    @Transactional
    public Optional<User> verifyUser(Long userId) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        var user = userOptional.get();
        user.verify();
        var verifiedUser = userRepository.save(user);
        return Optional.of(verifiedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        logger.info("Handling change password for user ID: " + userId);
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        var user = userOptional.get();
        if (!hashingService.matches(oldPassword, user.getPassword())) {
            logger.warning("Old password mismatch for user ID: " + userId);
            return false;
        }

        user.updatePassword(hashingService.encode(newPassword));
        userRepository.save(user);
        logger.info("Password changed successfully for user ID: " + userId);
        return true;
    }

    @Override
    @Transactional
    public Optional<User> updateUserTenantId(Long userId, Long tenantId) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        var user = userOptional.get();
        user.setTenantId(tenantId);
        return Optional.of(userRepository.save(user));
    }
}
