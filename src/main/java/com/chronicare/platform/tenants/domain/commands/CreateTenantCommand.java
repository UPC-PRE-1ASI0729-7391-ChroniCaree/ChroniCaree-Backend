package com.chronicare.platform.tenants.domain.commands;


import com.chronicare.platform.tenants.domain.valueobjects.TenantName;

/**
 * Command para crear un tenant.
 *
 * @param name nombre del tenant
 */
public record CreateTenantCommand(TenantName name) {

    public CreateTenantCommand {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
    }
}
