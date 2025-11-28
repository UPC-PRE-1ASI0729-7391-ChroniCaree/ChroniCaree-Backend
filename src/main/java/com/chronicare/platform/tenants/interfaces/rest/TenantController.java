package com.chronicare.platform.tenants.interfaces.rest;

 
import com.chronicare.platform.tenants.application.services.TenantService;
import com.chronicare.platform.tenants.domain.aggregates.Tenant;
import com.chronicare.platform.tenants.domain.commands.CreateTenantCommand;
import com.chronicare.platform.tenants.domain.commands.DeleteTenantCommand;
import com.chronicare.platform.tenants.domain.commands.UpdateTenantCommand;
import com.chronicare.platform.tenants.domain.valueobjects.TenantName;
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

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
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

    record UpdateTenantRequest(String name) {
        public UpdateTenantRequest {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name cannot be null or blank");
            }
        }
    }

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
        UpdateTenantCommand cmd = new UpdateTenantCommand(id, new TenantName(req.name()));
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
}
