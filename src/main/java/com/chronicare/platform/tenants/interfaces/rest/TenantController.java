package com.chronicare.platform.tenants.interfaces.rest;

 
import com.chronicare.platform.tenants.application.services.TenantService;
import com.chronicare.platform.tenants.domain.aggregates.Tenant;
import com.chronicare.platform.tenants.domain.commands.CreateTenantCommand;
import com.chronicare.platform.tenants.domain.commands.DeleteTenantCommand;
import com.chronicare.platform.tenants.domain.commands.UpdateTenantCommand;
import com.chronicare.platform.tenants.domain.valueobjects.TenantName;
import com.chronicare.platform.doctors.infrastructure.persistence.jpa.repositories.DoctorRepository;
import com.chronicare.platform.patients.domain.repository.PatientRepository;
import com.chronicare.platform.invitations.domain.model.queries.GetInvitationsByTenantIdAndStatusQuery;
import com.chronicare.platform.invitations.domain.services.InvitationQueryService;
import com.chronicare.platform.invitations.interfaces.rest.transform.InvitationResourceFromEntityAssembler;
import com.chronicare.platform.invitations.interfaces.rest.resources.InvitationResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TenantController - expone endpoints REST usando commands/queries.
 */
@RestController
@RequestMapping("/api/v1/tenants")
@Tag(name = "Tenants", description = "Tenant management API")
public class TenantController {

    private final TenantService tenantService;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final InvitationQueryService invitationQueryService;

    public TenantController(
            TenantService tenantService, 
            DoctorRepository doctorRepository, 
            PatientRepository patientRepository,
            InvitationQueryService invitationQueryService
    ) {
        this.tenantService = tenantService;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.invitationQueryService = invitationQueryService;
    }

    record TenantResponse(
        Long id, 
        String name,
        Long adminUserId,
        String email,
        String address,
        String phone,
        String status,
        Long subscriptionId,
        String registrationDate,
        TenantSettings settings
    ) { }

    record TenantSettings(
        Boolean allowIndependentDoctors,
        Boolean requirePatientApproval,
        Integer maxDoctors
    ) {}

    record CreateTenantRequest(
        Long adminUserId,
        String name,
        String email,
        String address,
        String phone,
        String status,
        Long subscriptionId,
        String registrationDate,
        TenantSettings settings
    ) {
        public CreateTenantRequest {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name cannot be null or blank");
            }
        }
    }

    record UpdateTenantRequest(
        String name,
        String email,
        String address,
        String phone,
        String status,
        Long subscriptionId,
        TenantSettings settings
    ) {}

    private TenantResponse toResponse(Tenant t) {
        return new TenantResponse(
            t.getId(),
            t.getName().value(),
            t.getAdminUserId(),
            t.getEmail(),
            t.getAddress(),
            t.getPhone(),
            t.getStatus(),
            t.getSubscriptionId(),
            t.getRegistrationDate() != null ? t.getRegistrationDate().toString() : null,
            new TenantSettings(
                t.getAllowIndependentDoctors(),
                t.getRequirePatientApproval(),
                t.getMaxDoctors()
            )
        );
    }

    @GetMapping
    @Operation(summary = "List all tenants")
    public ResponseEntity<List<TenantResponse>> getAllTenants() {
        List<TenantResponse> resp = tenantService.getAllTenants().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tenant by ID")
    public ResponseEntity<TenantResponse> getTenantById(@PathVariable Long id) {
        return tenantService.getTenantById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-admin/{userId}")
    @Operation(summary = "Get tenant by Admin User ID")
    public ResponseEntity<TenantResponse> getTenantByAdminUserId(@PathVariable Long userId) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TenantController.class.getName());
        logger.info("========== TenantController: GET /api/v1/tenants/by-admin/" + userId + " ==========");
        logger.info("Calling tenantService.getTenantByAdminUserId(" + userId + ")");
        
        var result = tenantService.getTenantByAdminUserId(userId)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warning("❌ Tenant not found for admin user ID: " + userId + ", returning 404");
                    return ResponseEntity.notFound().build();
                });
        
        if (result.getStatusCode().is2xxSuccessful()) {
            logger.info("✓ Returning tenant data with HTTP 200");
        }
        logger.info("======================================================================");
        
        return result;
    }

    record DashboardStats(long totalDoctors, long totalPatients, long activeAlerts) {}

    @GetMapping("/{id}/dashboard-stats")
    @Operation(summary = "Get dashboard stats")
    public ResponseEntity<DashboardStats> getDashboardStats(@PathVariable Long id) {
        long doctors = doctorRepository.countByTenantId(id);
        long patients = patientRepository.countByTenantId(id);
        return ResponseEntity.ok(new DashboardStats(doctors, patients, 0));
    }

    @PostMapping
    @Operation(summary = "Create new tenant")
    public ResponseEntity<TenantResponse> createTenant(@RequestBody CreateTenantRequest req) {
        java.time.LocalDateTime regDate = null;
        if (req.registrationDate() != null) {
            // Handle ISO format with Z (UTC) by parsing to Instant then converting to LocalDateTime
            try {
                regDate = java.time.ZonedDateTime.parse(req.registrationDate()).toLocalDateTime();
            } catch (Exception e) {
                // Fallback or try LocalDateTime parse
                try {
                    regDate = java.time.LocalDateTime.parse(req.registrationDate());
                } catch (Exception ex) {
                    regDate = java.time.LocalDateTime.now();
                }
            }
        } else {
            regDate = java.time.LocalDateTime.now();
        }

        CreateTenantCommand cmd = new CreateTenantCommand(
            req.adminUserId(),
            new TenantName(req.name()),
            req.email(),
            req.address(),
            req.phone(),
            req.status(),
            req.subscriptionId(),
            regDate,
            req.settings() != null ? req.settings().allowIndependentDoctors() : null,
            req.settings() != null ? req.settings().requirePatientApproval() : null,
            req.settings() != null ? req.settings().maxDoctors() : null
        );
        Tenant created = tenantService.createTenant(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tenant by ID")
    public ResponseEntity<TenantResponse> updateTenant(@PathVariable Long id, @RequestBody UpdateTenantRequest req) {
        TenantName tenantName = (req.name() != null && !req.name().isBlank()) ? new TenantName(req.name()) : null;
        
        UpdateTenantCommand cmd = new UpdateTenantCommand(
            id, 
            tenantName,
            req.email(),
            req.address(),
            req.phone(),
            req.status(),
            req.subscriptionId(),
            req.settings() != null ? req.settings().allowIndependentDoctors() : null,
            req.settings() != null ? req.settings().requirePatientApproval() : null,
            req.settings() != null ? req.settings().maxDoctors() : null
        );
        Tenant updated = tenantService.updateTenant(cmd);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tenant by ID")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        DeleteTenantCommand cmd = new DeleteTenantCommand(id);
        tenantService.deleteTenant(cmd);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/tenants/{id}/invitations/pending
     * Get pending invitations for a tenant
     */
    @GetMapping("/{id}/invitations/pending")
    @Operation(summary = "Get pending invitations for tenant")
    public ResponseEntity<List<InvitationResource>> getPendingInvitations(@PathVariable Long id) {
        var query = new GetInvitationsByTenantIdAndStatusQuery(id, "pending");
        var invitations = invitationQueryService.handle(query);
        
        List<InvitationResource> resources = invitations.stream()
                .map(InvitationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok(resources);
    }

    /**
     * GET /api/v1/tenants/{id}/invitations
     * Get all invitations for a tenant
     */
    @GetMapping("/{id}/invitations")
    @Operation(summary = "Get all invitations for tenant")
    public ResponseEntity<List<InvitationResource>> getAllInvitationsForTenant(
            @PathVariable Long id,
            @RequestParam(required = false) String status
    ) {
        List<InvitationResource> resources;
        
        if (status != null && !status.isEmpty()) {
            var query = new GetInvitationsByTenantIdAndStatusQuery(id, status);
            resources = invitationQueryService.handle(query).stream()
                    .map(InvitationResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
        } else {
            var query = new com.chronicare.platform.invitations.domain.model.queries.GetInvitationsByTenantIdQuery(id);
            resources = invitationQueryService.handle(query).stream()
                    .map(InvitationResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
        }
        
        return ResponseEntity.ok(resources);
    }
}
