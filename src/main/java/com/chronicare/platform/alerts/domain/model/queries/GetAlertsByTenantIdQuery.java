package com.chronicare.platform.alerts.domain.model.queries;

/**
 * Query to get alerts by tenant ID with optional filters
 */
public record GetAlertsByTenantIdQuery(
    Long tenantId,
    String status,
    String severity,
    String category,
    Integer page,
    Integer limit
) {
    public GetAlertsByTenantIdQuery {
        if (tenantId == null) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }

    public GetAlertsByTenantIdQuery(Long tenantId) {
        this(tenantId, null, null, null, 0, 20);
    }
}
