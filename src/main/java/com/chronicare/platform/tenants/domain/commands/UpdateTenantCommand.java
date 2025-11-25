package com.chronicare.platform.tenants.domain.commands;

import com.chronicare.platform.tenants.domain.valueobjects.TenantName;

/**
 * Command para actualizar un tenant.
 *
 * @param tenantId id del tenant
 * @param name nuevo nombre
 */
public record UpdateTenantCommand(Long tenantId, TenantName name) {

    public UpdateTenantCommand  {
        if (tenantId == null || tenantId <= 0) {
            throw new IllegalArgumentException("tenantId invalid");
        }
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
    }
}
