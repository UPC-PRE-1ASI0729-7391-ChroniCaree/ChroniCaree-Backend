package com.chronicare.platform.tenants.domain.model.queries;

/**
 * Query to get tenant dashboard statistics
 */
public record GetTenantDashboardStatsQuery(Long tenantId) {
    public GetTenantDashboardStatsQuery {
        if (tenantId == null) {
            throw new IllegalArgumentException("tenantId cannot be null");
        }
    }
}
