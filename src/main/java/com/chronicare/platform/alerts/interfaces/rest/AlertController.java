package com.chronicare.platform.alerts.interfaces.rest;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.commands.AcknowledgeAlertCommand;
import com.chronicare.platform.alerts.domain.model.commands.CreateAlertCommand;
import com.chronicare.platform.alerts.domain.model.commands.DismissAlertCommand;
import com.chronicare.platform.alerts.domain.model.commands.EscalateAlertCommand;
import com.chronicare.platform.alerts.domain.model.commands.ResolveAlertCommand;
import com.chronicare.platform.alerts.domain.model.queries.GetAlertByIdQuery;
import com.chronicare.platform.alerts.domain.model.queries.GetAlertsByDoctorIdQuery;
import com.chronicare.platform.alerts.domain.model.queries.GetAlertsByPatientIdQuery;
import com.chronicare.platform.alerts.domain.model.queries.GetAlertsByTenantIdQuery;
import com.chronicare.platform.alerts.domain.repository.AlertRepository;
import com.chronicare.platform.alerts.domain.services.AlertCommandService;
import com.chronicare.platform.alerts.domain.services.AlertQueryService;
import com.chronicare.platform.alerts.interfaces.rest.resources.*;
import com.chronicare.platform.alerts.interfaces.rest.transform.AlertResourceFromEntityAssembler;
import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.queries.GetPatientByIdQuery;
import com.chronicare.platform.patients.domain.services.PatientQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Alert operations following DDD pattern
 */
@RestController
@RequestMapping(value = "/api/v1/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Alerts", description = "Alert management API")
public class AlertController {

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
    private static final String STATUS_ACTIVE = "ACTIVE";

    private final AlertCommandService alertCommandService;
    private final AlertQueryService alertQueryService;
    private final PatientQueryService patientQueryService;
    private final AlertRepository alertRepository;
    private final com.chronicare.platform.alerts.infrastructure.events.DomainEventPublisher eventPublisher;

    public AlertController(AlertCommandService alertCommandService, 
                          AlertQueryService alertQueryService, 
                          PatientQueryService patientQueryService,
                          AlertRepository alertRepository,
                          com.chronicare.platform.alerts.infrastructure.events.DomainEventPublisher eventPublisher) {
        this.alertCommandService = alertCommandService;
        this.alertQueryService = alertQueryService;
        this.patientQueryService = patientQueryService;
        this.alertRepository = alertRepository;
        this.eventPublisher = eventPublisher;
    }

    // ==================== CREATE ====================
    
    @PostMapping
    @Operation(summary = "Create new alert")
    public ResponseEntity<AlertResource> createAlert(@RequestBody CreateAlertResource resource) {
        try {
            if (resource.patientId() == null) {
                return ResponseEntity.badRequest().build();
            }

            Patient patient = patientQueryService.handle(new GetPatientByIdQuery(resource.patientId()))
                    .orElse(null);
            if (patient == null) {
                return ResponseEntity.badRequest().build();
            }

            Long doctorId = resource.doctorId() != null ? resource.doctorId() : patient.getAssignedDoctorId();
            Long tenantId = resource.tenantId() != null ? resource.tenantId() : patient.getTenantId();

            String type = resource.type() != null && !resource.type().isBlank()
                    ? resource.type()
                    : "symptom_worsening";
            String severity = resource.severity() != null && !resource.severity().isBlank()
                    ? resource.severity()
                    : "medium";
            String title = resource.title() != null && !resource.title().isBlank()
                    ? resource.title()
                    : "Alert";

            var command = new CreateAlertCommand(
                    resource.patientId(),
                    doctorId,
                    tenantId,
                    type,
                    severity,
                    resource.category(),
                    title,
                    resource.message(),
                    resource.description(),
                    "MANUAL", // source (default for manual creation via REST)
                    resource.sourceType(),
                    resource.sourceId(),
                    java.time.LocalDateTime.now(), // detectedAt
                    resource.metadata(),
                    resource.priority(),
                    resource.expiresAt()
            );

            Alert alert = alertCommandService.handle(command);
            return new ResponseEntity<>(AlertResourceFromEntityAssembler.toResourceFromEntity(alert), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            logger.warn("Invalid alert payload: {}", ex.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            logger.error("Error creating alert", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== READ ====================
    
    @GetMapping
    @Operation(summary = "Get all alerts with optional filters")
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'HOSPITAL_ADMIN', 'SYSTEM', 'PATIENT', 'TENANT_ADMIN')")
        public ResponseEntity<List<AlertResource>> getAllAlerts(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        try {
            logger.info("Fetching alerts with filters - patientId: {}, doctorId: {}, status: {}, severity: {}", 
                patientId, doctorId, status, severity);
            
            List<Alert> alerts;
            
            // Filter by patientId if provided (supports pagination and case-insensitive filters)
            if (patientId != null) {
                String normalizedStatus = status != null && !status.isBlank() ? status.trim() : null;
                String normalizedSeverity = severity != null && !severity.isBlank() ? severity.trim() : null;
                var query = new GetAlertsByPatientIdQuery(patientId, normalizedStatus, normalizedSeverity, null, page, limit);
                alerts = alertQueryService.handle(query);
            }
            // Filter by doctorId if provided
            else if (doctorId != null) {
                var query = new GetAlertsByDoctorIdQuery(doctorId, status, severity, null, null, 0, 1000, "createdAt DESC");
                var pageResult = alertQueryService.handle(query);
                alerts = pageResult.getContent(); // Extract list from Page
            }
            // No filters - get all alerts
            else {
                alerts = alertQueryService.getAllAlerts();
            }
            
            logger.info("Found {} alerts", alerts.size());
            
            List<AlertResource> resources = alerts.stream()
                .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
            
            return ResponseEntity.ok(resources);
        } catch (Exception ex) {
            logger.error("Error fetching alerts", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }
    
    @GetMapping("/{alertId}")
    @Operation(summary = "Get alert by ID")
    public ResponseEntity<AlertResource> getAlertById(@PathVariable Long alertId) {
        var query = new GetAlertByIdQuery(alertId);
        Optional<Alert> result = alertQueryService.handle(query);
        return result
            .map(alert -> ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(alert)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get alerts by patient ID")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'HOSPITAL_ADMIN', 'SYSTEM', 'PATIENT', 'TENANT_ADMIN')")
    public ResponseEntity<List<AlertResource>> getAlertsByPatientId(
            @PathVariable Long patientId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity) {
        
        var query = new GetAlertsByPatientIdQuery(patientId, status, severity, null, 0, 100);
        List<Alert> alerts = alertQueryService.handle(query);
        
        List<AlertResource> resources = alerts.stream()
            .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get alerts by doctor ID")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'HOSPITAL_ADMIN', 'SYSTEM', 'PATIENT', 'TENANT_ADMIN')")
    public ResponseEntity<List<AlertResource>> getAlertsByDoctorId(
            @PathVariable Long doctorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) Long patientId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        
        var query = new GetAlertsByDoctorIdQuery(doctorId, status, severity, null, patientId, page, limit, "createdAt DESC");
        var alertPage = alertQueryService.handle(query);
        
        List<AlertResource> resources = alertPage.getContent().stream()
            .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/tenant/{tenantId}")
    @Operation(summary = "Get alerts by tenant ID")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'HOSPITAL_ADMIN', 'SYSTEM', 'PATIENT', 'TENANT_ADMIN')")
    public ResponseEntity<List<AlertResource>> getAlertsByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity) {
        
        var query = new GetAlertsByTenantIdQuery(tenantId, status, severity, null, 0, 100);
        List<Alert> alerts = alertQueryService.handle(query);
        
        List<AlertResource> resources = alerts.stream()
            .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(resources);
    }

    // ==================== ACTIONS ====================
    
    @PutMapping("/{alertId}/acknowledge")
    @Operation(summary = "Acknowledge an alert")
    public ResponseEntity<AlertResource> acknowledgeAlert(
            @PathVariable Long alertId,
            @RequestParam Long userId,
            @RequestBody(required = false) AcknowledgeAlertResource resource) {
        
        String notes = resource != null ? resource.notes() : null;
        var command = new AcknowledgeAlertCommand(alertId, userId, notes);
        Optional<Alert> result = alertCommandService.handle(command);
        
        return result
            .map(alert -> ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(alert)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{alertId}/resolve")
    @Operation(summary = "Resolve an alert")
    public ResponseEntity<AlertResource> resolveAlert(
            @PathVariable Long alertId,
            @RequestParam Long userId,
            @RequestBody(required = false) ResolveAlertResource resource) {
        
        String notes = resource != null ? resource.notes() : null;
        String action = resource != null ? resource.resolutionAction() : null;
        var command = new ResolveAlertCommand(alertId, userId, notes, action);
        Optional<Alert> result = alertCommandService.handle(command);
        
        return result
            .map(alert -> ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(alert)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{alertId}/escalate")
    @Operation(summary = "Escalate an alert")
    public ResponseEntity<AlertResource> escalateAlert(
            @PathVariable Long alertId,
            @RequestBody EscalateAlertResource resource) {
        
        var command = new EscalateAlertCommand(alertId, resource.escalateTo(), null, resource.reason());
        Optional<Alert> result = alertCommandService.handle(command);
        
        return result
            .map(alert -> ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(alert)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{alertId}/dismiss")
    @Operation(summary = "Dismiss an alert")
    public ResponseEntity<AlertResource> dismissAlert(
            @PathVariable Long alertId,
            @RequestParam Long userId,
            @RequestBody(required = false) DismissAlertResource resource) {
        
        String reason = resource != null ? resource.reason() : null;
        var command = new DismissAlertCommand(alertId, userId, reason);
        Optional<Alert> result = alertCommandService.handle(command);
        
        return result
            .map(alert -> ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(alert)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ==================== DELETE ====================
    
    @DeleteMapping("/{alertId}")
    @Operation(summary = "Delete alert by ID")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long alertId) {
        var query = new GetAlertByIdQuery(alertId);
        Optional<Alert> existing = alertQueryService.handle(query);
        
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        alertCommandService.deleteAlert(alertId);
        return ResponseEntity.noContent().build();
    }

    // ==================== STATS ====================
    
    @GetMapping("/tenant/{tenantId}/stats")
    @Operation(summary = "Get alert statistics for a tenant")
    public ResponseEntity<AlertStatsResource> getAlertStats(@PathVariable Long tenantId) {
        // Get counts using query service methods
        long activeCount = alertQueryService.countActiveAlertsByTenantId(tenantId);
        long criticalCount = alertQueryService.countCriticalAlertsByTenantId(tenantId);
        
        // Get detailed counts
        var acknowledgedQuery = new GetAlertsByTenantIdQuery(tenantId, "ACKNOWLEDGED", null, null, 0, 1000);
        var resolvedQuery = new GetAlertsByTenantIdQuery(tenantId, "RESOLVED", null, null, 0, 1000);
        var escalatedQuery = new GetAlertsByTenantIdQuery(tenantId, "ESCALATED", null, null, 0, 1000);
        
        int acknowledgedCount = alertQueryService.handle(acknowledgedQuery).size();
        int resolvedCount = alertQueryService.handle(resolvedQuery).size();
        int escalatedCount = alertQueryService.handle(escalatedQuery).size();
        
        // Get high severity count from active alerts
        var highQuery = new GetAlertsByTenantIdQuery(tenantId, STATUS_ACTIVE, "HIGH", null, 0, 1000);
        int highCount = alertQueryService.handle(highQuery).size();
        
        AlertStatsResource stats = new AlertStatsResource(
            (int) activeCount,
            acknowledgedCount,
            resolvedCount,
            escalatedCount,
            (int) criticalCount,
            highCount,
            (int) activeCount + acknowledgedCount + escalatedCount // total pending
        );
        
        return ResponseEntity.ok(stats);
    }

    // ==================== NEW ENDPOINTS (2025 SPEC) ====================

    /**
     * POST /api/v1/alerts/{alertId}/assign - Assign alert to user
     */
    @PostMapping("/{alertId}/assign")
    @Operation(summary = "Assign alert to user")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'HOSPITAL_ADMIN', 'TENANT_ADMIN')")
    public ResponseEntity<AlertResource> assignAlert(
            @PathVariable Long alertId,
            @RequestBody AssignAlertRequest request) {
        
        Optional<Alert> alertOpt = alertQueryService.handle(new GetAlertByIdQuery(alertId));
        if (alertOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Alert alert = alertOpt.get();
        alert.assign(request.userId(), request.role());
        Alert updatedAlert = alertRepository.save(alert);
        
        // Publish event (WebSocket broadcast disabled - needs spring-boot-starter-websocket dependency)
        var event = new com.chronicare.platform.alerts.domain.model.events.AlertAssignedEvent(
            updatedAlert.getId(),
            updatedAlert.getPatientId(),
            updatedAlert.getTenantId(),
            request.userId(),
            request.role()
        );
        eventPublisher.publish(event);
        
        return ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(updatedAlert));
    }

    /**
     * POST /api/v1/alerts/suppress - Suppress alerts for a tenant/patient
     */
    @PostMapping("/suppress")
    @Operation(summary = "Suppress alerts temporarily")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCTOR', 'HOSPITAL_ADMIN', 'TENANT_ADMIN')")
    public ResponseEntity<Void> suppressAlerts(@RequestBody SuppressAlertsRequest request) {
        
        // Get all active alerts for patient or tenant
        var query = request.patientId() != null ?
            new GetAlertsByPatientIdQuery(request.patientId(), STATUS_ACTIVE, null, null, 0, 1000) :
            new GetAlertsByTenantIdQuery(request.tenantId(), STATUS_ACTIVE, null, null, 0, 1000);
        
        List<Alert> alerts = request.patientId() != null ?
            alertQueryService.handle((GetAlertsByPatientIdQuery) query) :
            alertQueryService.handle((GetAlertsByTenantIdQuery) query);

        // Suppress each alert using dismiss command
        for (Alert alert : alerts) {
            DismissAlertCommand dismissCmd = new DismissAlertCommand(alert.getId(), 0L, request.reason());
            alertCommandService.handle(dismissCmd);
        }
        
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/v1/alerts/webhook - External webhook for alert ingestion
     */
    @PostMapping("/webhook")
    @Operation(summary = "Webhook endpoint for external alert sources")
    public ResponseEntity<AlertResource> webhookAlert(
            @RequestBody CreateAlertResource resource,
            @RequestHeader(value = "X-Webhook-Signature", required = false) String signature) {
        
        try {
            var command = new CreateAlertCommand(
                resource.patientId(),
                resource.doctorId(),
                resource.tenantId(),
                resource.type(),
                resource.severity(),
                resource.category(),
                resource.title(),
                resource.message(),
                resource.description(),
                "SYSTEM", // source
                resource.sourceType(),
                resource.sourceId(),
                java.time.LocalDateTime.now(), // detectedAt
                resource.metadata(),
                resource.priority(),
                resource.expiresAt()
            );
            
            Alert createdAlert = alertCommandService.handle(command);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(AlertResourceFromEntityAssembler.toResourceFromEntity(createdAlert));
        } catch (Exception ex) {
            logger.error("Error processing webhook alert", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== REQUEST DTOs ====================

    public record AssignAlertRequest(Long userId, String role) {}
    
    public record SuppressAlertsRequest(
        Long tenantId,
        Long patientId,
        java.time.LocalDateTime until,
        String reason
    ) {}
}
