package com.chronicare.platform.tenants.domain.commands;

/**
 * Command para eliminar un tenant.
 *
 * @param tenantId id del tenant
 */
public record DeleteTenantCommand(Long tenantId) {

    public DeleteTenantCommand {
        if (tenantId == null || tenantId <= 0) {
            throw new IllegalArgumentException("tenantId invalid");
        }
    }
}
