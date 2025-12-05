package com.chronicare.platform.patients.interfaces.rest;

import com.chronicare.platform.patients.domain.model.queries.GetPatientDashboardQuery;
import com.chronicare.platform.patients.domain.services.PatientQueryService;
import com.chronicare.platform.patients.interfaces.rest.resources.PatientDashboardResource;
import com.chronicare.platform.patients.interfaces.rest.transform.PatientDashboardResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Patient Dashboard Controller
 * Exposes endpoints for patient dashboard data
 */
@RestController
@RequestMapping("/api/v1/patients/{patientId}/dashboard")
@Tag(name = "Patient Dashboard", description = "Patient Dashboard Endpoints")
public class PatientDashboardController {
    
    private final PatientQueryService patientQueryService;
    
    public PatientDashboardController(PatientQueryService patientQueryService) {
        this.patientQueryService = patientQueryService;
    }
    
    /**
     * Get patient dashboard data
     * @param patientId Patient ID
     * @return Patient dashboard with appointments, medications, alerts, vital signs
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'HOSPITAL_ADMIN')")
    public ResponseEntity<PatientDashboardResource> getPatientDashboard(@PathVariable Long patientId) {
        var query = new GetPatientDashboardQuery(patientId);
        var dashboard = patientQueryService.handle(query);
        
        if (dashboard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = PatientDashboardResourceFromEntityAssembler.toResourceFromEntity(dashboard.get());
        return ResponseEntity.ok(resource);
    }
}
