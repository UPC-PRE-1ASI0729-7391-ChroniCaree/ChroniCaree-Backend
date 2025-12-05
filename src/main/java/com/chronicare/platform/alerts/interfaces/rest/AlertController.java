package com.chronicare.platform.alerts.interfaces.rest;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.commands.AcknowledgeAlertCommand;
import com.chronicare.platform.alerts.domain.model.commands.DismissAlertCommand;
import com.chronicare.platform.alerts.domain.model.commands.EscalateAlertCommand;
import com.chronicare.platform.alerts.domain.model.commands.ResolveAlertCommand;
import com.chronicare.platform.alerts.domain.model.queries.GetAlertByIdQuery;
import com.chronicare.platform.alerts.domain.model.queries.GetAlertsByDoctorIdQuery;
import com.chronicare.platform.alerts.domain.model.queries.GetAlertsByPatientIdQuery;
import com.chronicare.platform.alerts.domain.model.queries.GetAlertsByTenantIdQuery;
import com.chronicare.platform.alerts.domain.services.AlertCommandService;
import com.chronicare.platform.alerts.domain.services.AlertQueryService;
import com.chronicare.platform.alerts.interfaces.rest.resources.*;
import com.chronicare.platform.alerts.interfaces.rest.transform.AlertResourceFromEntityAssembler;
import com.chronicare.platform.alerts.interfaces.rest.transform.CreateAlertCommandFromResourceAssembler;
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

    private final AlertCommandService alertCommandService;
    private final AlertQueryService alertQueryService;

    public AlertController(AlertCommandService alertCommandService, AlertQueryService alertQueryService) {
        this.alertCommandService = alertCommandService;
        this.alertQueryService = alertQueryService;
    }

    // ==================== CREATE ====================
    
    @PostMapping
    @Operation(summary = "Create new alert")
    public ResponseEntity<AlertResource> createAlert(@RequestBody CreateAlertResource resource) {
        var command = CreateAlertCommandFromResourceAssembler.toCommandFromResource(resource);
        Alert alert = alertCommandService.handle(command);
        return new ResponseEntity<>(AlertResourceFromEntityAssembler.toResourceFromEntity(alert), HttpStatus.CREATED);
    }

    // ==================== READ ====================
    
    @GetMapping
    @Operation(summary = "Get all alerts with optional filters")
    public ResponseEntity<List<AlertResource>> getAllAlerts(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity) {
        try {
            logger.info("Fetching alerts with filters - patientId: {}, doctorId: {}, status: {}, severity: {}", 
                patientId, doctorId, status, severity);
            
            List<Alert> alerts;
            
            // Filter by patientId if provided
            if (patientId != null) {
                var query = new GetAlertsByPatientIdQuery(patientId, status, severity, null, 0, 1000);
                alerts = alertQueryService.handle(query);
            }
            // Filter by doctorId if provided
            else if (doctorId != null) {
                var query = new GetAlertsByDoctorIdQuery(doctorId, status, severity, null, null, 0, 1000, "createdAt DESC");
                var page = alertQueryService.handle(query);
                alerts = page.getContent(); // Extract list from Page
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
        var highQuery = new GetAlertsByTenantIdQuery(tenantId, "ACTIVE", "HIGH", null, 0, 1000);
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
}
