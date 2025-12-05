package com.chronicare.platform.tenants.domain.services;

import com.chronicare.platform.tenants.domain.model.aggregates.DashboardStats;
import com.chronicare.platform.tenants.domain.model.queries.GetTenantDashboardStatsQuery;

import java.util.Optional;

/**
 * Tenant Query Service
 * Handles read operations for tenants
 */
public interface TenantQueryService {
    
    /**
     * Get dashboard statistics for a tenant
     * @param query The query containing tenantId
     * @return DashboardStats with comprehensive tenant statistics
     */
    Optional<DashboardStats> handle(GetTenantDashboardStatsQuery query);
}
