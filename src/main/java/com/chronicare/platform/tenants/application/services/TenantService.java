package com.chronicare.platform.tenants.application.services;

import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.repositories.UserRepository;
import com.chronicare.platform.tenants.domain.aggregates.Tenant;
import com.chronicare.platform.tenants.domain.commands.CreateTenantCommand;
import com.chronicare.platform.tenants.domain.commands.DeleteTenantCommand;
import com.chronicare.platform.tenants.domain.commands.UpdateTenantCommand;
import com.chronicare.platform.tenants.domain.events.TenantCreatedEvent;
import com.chronicare.platform.tenants.domain.repository.TenantRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * TenantService
 *
 * @summary
 * Application-level service handling tenant lifecycle operations, enforcing
 * validation rules and coordinating domain updates.
 * Responsibilities:
 * - Retrieve tenants and resolve by admin user
 * - Create tenants with role validation, name uniqueness, and admin ownership rules
 * - Update tenant properties with field-level validations
 * - Delete tenants safely with existence checks
 * - Synchronize tenant–admin relationships by updating the admin user's tenantId
 * - Publish domain events (TenantCreatedEvent) upon creation
 * Notes:
 * - Ensures an admin user can own only one tenant
 * - Enforces unique tenant names across the platform
 * - Uses transactional boundaries to guarantee consistency
 */

@Service
@Transactional
public class TenantService {
    private static final Logger logger = Logger.getLogger(TenantService.class.getName());
    private static final Set<String> ALLOWED_STATUS = Set.of("pending_subscription", "active", "suspended", "cancelled");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{6,15}$");

    private final TenantRepository repository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TenantService(TenantRepository repository, UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Tenant> getAllTenants() {
        return repository.findAll();
    }

    public Optional<Tenant> getTenantById(Long id) {
        return repository.findById(id);
    }

    public Optional<Tenant> getTenantByAdminUserId(Long adminUserId) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "========== TenantService: getTenantByAdminUserId ==========");
            logger.log(Level.INFO, "  - Searching for tenant with adminUserId: {0}", adminUserId);
        }
        
        Optional<Tenant> result = repository.findByAdminUserId(adminUserId);
        
        if (result.isPresent()) {
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, "✓ Tenant FOUND:");
                logger.log(Level.INFO, "  - Tenant ID: {0}", result.get().getId());
                logger.log(Level.INFO, "  - Tenant Name: {0}", result.get().getName());
                logger.log(Level.INFO, "  - Admin User ID: {0}", result.get().getAdminUserId());
                logger.log(Level.INFO, "  - Status: {0}", result.get().getStatus());
            }
        } else {
            logger.log(Level.WARNING, "❌ Tenant NOT FOUND for adminUserId: {0}", adminUserId);
            
            // Debug: List ALL tenants to see what exists
            List<Tenant> allTenants = repository.findAll();
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, "Total tenants in database: {0}", allTenants.size());
                if (!allTenants.isEmpty()) {
                    logger.log(Level.INFO, "All tenants:");
                    for (Tenant t : allTenants) {
                        logger.log(Level.INFO, "  - ID: {0}, Name: {1}, AdminUserId: {2}", new Object[]{t.getId(), t.getName(), t.getAdminUserId()});
                    }
                }
            }
        }
        
        logger.log(Level.INFO, "============================================================");
        return result;
    }

    public Tenant createTenant(CreateTenantCommand command) {

        User adminUser = userRepository.findById(command.adminUserId())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found with ID: " + command.adminUserId()));
        
        if (!"HOSPITAL_ADMIN".equalsIgnoreCase(adminUser.getRole().getName())) {
            throw new IllegalArgumentException("User is not a hospital_admin");
        }
        

        if (repository.findByAdminUserId(command.adminUserId()).isPresent()) {
            throw new IllegalArgumentException("Admin user already has a tenant");
        }

        if (repository.findByName(command.name().value()).isPresent()) {
            throw new IllegalArgumentException("Tenant name already exists");
        }

        String normalizedStatus = command.status() != null ? command.status().toLowerCase() : "pending_subscription";
        if (!ALLOWED_STATUS.contains(normalizedStatus)) {
            throw new IllegalArgumentException("Invalid status value. Allowed: " + ALLOWED_STATUS);
        }
        
        Tenant tenant = Tenant.builder()
                .name(command.name().value())
                .adminUserId(command.adminUserId())
                .email(command.email())
                .address(command.address())
                .phone(command.phone())
                .status(normalizedStatus)
                .subscriptionId(command.subscriptionId())
                .registrationDate(command.registrationDate())
                .allowIndependentDoctors(command.allowIndependentDoctors())
                .requirePatientApproval(command.requirePatientApproval())
                .maxDoctors(command.maxDoctors())
                .build();
                
        Tenant saved = repository.save(tenant);

        adminUser.setTenantId(saved.getId());
        userRepository.save(adminUser);

        eventPublisher.publishEvent(new TenantCreatedEvent(this, saved.getId(), saved.getName()));
        return saved;
    }

    public Tenant updateTenant(UpdateTenantCommand command) {
        Tenant existing = repository.findById(command.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        updateAdminUser(existing, command);
        updateName(existing, command);
        updateEmail(existing, command);
        updateAddress(existing, command);
        updatePhone(existing, command);
        updateStatus(existing, command);
        updateSubscription(existing, command);
        updateSettings(existing, command);

        return repository.save(existing);
    }

    private void updateAdminUser(Tenant existing, UpdateTenantCommand command) {
        if (command.adminUserId() != null) {
            userRepository.findById(command.adminUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Admin user not found with ID: " + command.adminUserId()));
            existing.setAdminUserId(command.adminUserId());
        }
    }

    private void updateName(Tenant existing, UpdateTenantCommand command) {
        if (command.name() != null) {
            var maybeByName = repository.findByName(command.name().value());
            if (maybeByName.isPresent() && !maybeByName.get().getId().equals(existing.getId())) {
                throw new IllegalArgumentException("Tenant name already in use by another tenant");
            }
            existing.updateName(command.name());
        }
    }

    private void updateEmail(Tenant existing, UpdateTenantCommand command) {
        if (command.email() != null) {
            if (!EMAIL_PATTERN.matcher(command.email()).matches()) {
                throw new IllegalArgumentException("Invalid email format");
            }
            existing.setEmail(command.email());
        }
    }

    private void updateAddress(Tenant existing, UpdateTenantCommand command) {
        if (command.address() != null) {
            existing.setAddress(command.address());
        }
    }

    private void updatePhone(Tenant existing, UpdateTenantCommand command) {
        if (command.phone() != null) {
            if (!PHONE_PATTERN.matcher(command.phone()).matches()) {
                throw new IllegalArgumentException("Invalid phone format (digits 6-15)");
            }
            existing.setPhone(command.phone());
        }
    }

    private void updateStatus(Tenant existing, UpdateTenantCommand command) {
        if (command.status() != null) {
            String normalizedStatus = command.status().toLowerCase();
            if (!ALLOWED_STATUS.contains(normalizedStatus)) {
                throw new IllegalArgumentException("Invalid status value. Allowed: " + ALLOWED_STATUS);
            }
            existing.setStatus(normalizedStatus);
        }
    }

    private void updateSubscription(Tenant existing, UpdateTenantCommand command) {
        if (command.subscriptionId() != null) {
            // Allow setting subscription even if it doesn't exist yet (will be created later in the flow)
            existing.setSubscriptionId(command.subscriptionId());
        }
    }

    private void updateSettings(Tenant existing, UpdateTenantCommand command) {
        if (command.allowIndependentDoctors() != null) {
            existing.setAllowIndependentDoctors(command.allowIndependentDoctors());
        }
        if (command.requirePatientApproval() != null) {
            existing.setRequirePatientApproval(command.requirePatientApproval());
        }
        if (command.maxDoctors() != null) {
            existing.setMaxDoctors(command.maxDoctors());
        }
    }

    public void deleteTenant(DeleteTenantCommand command) {
        if (!repository.existsById(command.tenantId())) {
            throw new IllegalArgumentException("Tenant not found");
        }
        repository.deleteById(command.tenantId());

    }
}
