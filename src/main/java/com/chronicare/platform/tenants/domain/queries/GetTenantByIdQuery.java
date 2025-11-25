package com.chronicare.platform.tenants.domain.queries;

/**
 * Query para obtener tenant por id.
 */
public record GetTenantByIdQuery(Long tenantId) {

    public GetTenantByIdQuery {
        if (tenantId == null || tenantId <= 0) {
            throw new IllegalArgumentException("tenantId invalid");
        }
    }
}
