package com.chronicare.platform.tenants.interfaces.rest;

import com.chronicare.platform.tenants.domain.model.queries.GetTenantDashboardStatsQuery;
import com.chronicare.platform.tenants.domain.services.TenantQueryService;
import com.chronicare.platform.tenants.interfaces.rest.resources.DashboardStatsResource;
import com.chronicare.platform.tenants.interfaces.rest.transform.DashboardStatsResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Dashboard Controller
 * Exposes endpoints for tenant dashboard statistics
 */
@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/dashboard-stats")
@Tag(name = "Dashboard", description = "Dashboard Statistics Endpoints")
public class DashboardController {
    
    private final TenantQueryService tenantQueryService;
    
    public DashboardController(TenantQueryService tenantQueryService) {
        this.tenantQueryService = tenantQueryService;
    }
    
    /**
     * Get tenant dashboard statistics
     * @param tenantId Tenant ID
     * @return Dashboard statistics with all metrics
     */
    @GetMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    public ResponseEntity<DashboardStatsResource> getDashboardStats(@PathVariable Long tenantId) {
        var query = new GetTenantDashboardStatsQuery(tenantId);
        var stats = tenantQueryService.handle(query);
        
        if (stats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = DashboardStatsResourceFromEntityAssembler.toResourceFromEntity(stats.get());
        return ResponseEntity.ok(resource);
    }
}
