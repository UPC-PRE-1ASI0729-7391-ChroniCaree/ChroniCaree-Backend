package com.chronicare.platform.doctors.interfaces.rest;

import com.chronicare.platform.doctors.domain.model.queries.GetDoctorDashboardQuery;
import com.chronicare.platform.doctors.domain.services.DoctorQueryService;
import com.chronicare.platform.doctors.interfaces.rest.resources.DoctorDashboardResource;
import com.chronicare.platform.doctors.interfaces.rest.transform.DoctorDashboardResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Summary: Doctor Dashboard Controller
 * Exposes endpoints for doctor dashboard data
 */
@RestController
@RequestMapping("/api/v1/doctors/{doctorId}/dashboard")
@Tag(name = "Doctor Dashboard", description = "Doctor Dashboard Endpoints")
public class DoctorDashboardController {
    
    private final DoctorQueryService doctorQueryService;
    
    public DoctorDashboardController(DoctorQueryService doctorQueryService) {
        this.doctorQueryService = doctorQueryService;
    }
    
    /**
     * Get doctor dashboard data
     * @param doctorId Doctor ID
     * @return Doctor dashboard with today's appointments, patients, stats
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'HOSPITAL_ADMIN')")
    public ResponseEntity<DoctorDashboardResource> getDoctorDashboard(@PathVariable Long doctorId) {
        var query = new GetDoctorDashboardQuery(doctorId);
        var dashboard = doctorQueryService.handle(query);
        
        if (dashboard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = DoctorDashboardResourceFromEntityAssembler.toResourceFromEntity(dashboard.get());
        return ResponseEntity.ok(resource);
    }
}
