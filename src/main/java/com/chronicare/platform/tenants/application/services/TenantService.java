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
import java.util.logging.Logger;

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
        logger.info("========== TenantService: getTenantByAdminUserId ==========");
        logger.info("  - Searching for tenant with adminUserId: " + adminUserId);
        
        Optional<Tenant> result = repository.findByAdminUserId(adminUserId);
        
        if (result.isPresent()) {
            logger.info("✓ Tenant FOUND:");
            logger.info("  - Tenant ID: " + result.get().getId());
            logger.info("  - Tenant Name: " + result.get().getName());
            logger.info("  - Admin User ID: " + result.get().getAdminUserId());
            logger.info("  - Status: " + result.get().getStatus());
        } else {
            logger.warning("❌ Tenant NOT FOUND for adminUserId: " + adminUserId);
            
            // Debug: List ALL tenants to see what exists
            List<Tenant> allTenants = repository.findAll();
            logger.info("Total tenants in database: " + allTenants.size());
            if (!allTenants.isEmpty()) {
                logger.info("All tenants:");
                for (Tenant t : allTenants) {
                    logger.info("  - ID: " + t.getId() + ", Name: " + t.getName() + ", AdminUserId: " + t.getAdminUserId());
                }
            }
        }
        
        logger.info("============================================================");
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

        Tenant tenant = Tenant.builder()
                .name(command.name().value())
                .adminUserId(command.adminUserId())
                .email(command.email())
                .address(command.address())
                .phone(command.phone())
                .status(command.status() != null ? command.status() : "PENDING")
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

        if (command.name() != null) {
            var maybeByName = repository.findByName(command.name().value());
            if (maybeByName.isPresent() && !maybeByName.get().getId().equals(existing.getId())) {
                throw new IllegalArgumentException("Tenant name already in use by another tenant");
            }
            existing.updateName(command.name());
        }

        if (command.email() != null) {
            existing.setEmail(command.email());
        }
        if (command.address() != null) {
            existing.setAddress(command.address());
        }
        if (command.phone() != null) {
            existing.setPhone(command.phone());
        }
        if (command.status() != null) {
            existing.setStatus(command.status());
        }
        if (command.subscriptionId() != null) {
            existing.setSubscriptionId(command.subscriptionId());
        }
        if (command.allowIndependentDoctors() != null) {
            existing.setAllowIndependentDoctors(command.allowIndependentDoctors());
        }
        if (command.requirePatientApproval() != null) {
            existing.setRequirePatientApproval(command.requirePatientApproval());
        }
        if (command.maxDoctors() != null) {
            existing.setMaxDoctors(command.maxDoctors());
        }
        
        return repository.save(existing);
    }

    public void deleteTenant(DeleteTenantCommand command) {
        if (!repository.existsById(command.tenantId())) {
            throw new IllegalArgumentException("Tenant not found");
        }
        repository.deleteById(command.tenantId());

    }
}
