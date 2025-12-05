package com.chronicare.platform.tenants.domain.commands;

import com.chronicare.platform.tenants.domain.valueobjects.TenantName;

/**
 * Command para actualizar un tenant.
 *
 * @param tenantId id del tenant
 * @param name nuevo nombre (opcional)
 * @param email nuevo email (opcional)
 * @param address nueva dirección (opcional)
 * @param phone nuevo teléfono (opcional)
 * @param status nuevo estado (opcional)
 * @param subscriptionId nuevo ID de suscripción (opcional)
 */
public record UpdateTenantCommand(
    Long tenantId, 
    TenantName name,
    String email,
    String address,
    String phone,
    String status,
    Long subscriptionId,
    Boolean allowIndependentDoctors,
    Boolean requirePatientApproval,
    Integer maxDoctors
) {
    public UpdateTenantCommand  {
        if (tenantId == null || tenantId <= 0) {
            throw new IllegalArgumentException("tenantId invalid");
        }
    }
    
    // Constructor simplificado para solo actualizar el nombre (compatibilidad hacia atrás)
    public UpdateTenantCommand(Long tenantId, TenantName name) {
        this(tenantId, name, null, null, null, null, null, null, null, null);
    }
}
