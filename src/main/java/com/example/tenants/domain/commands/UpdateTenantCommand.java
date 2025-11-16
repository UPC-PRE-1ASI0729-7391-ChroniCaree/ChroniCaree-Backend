/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.tenants.domain.commands;

 

import com.example.tenants.domain.valueobjects.TenantName;

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
