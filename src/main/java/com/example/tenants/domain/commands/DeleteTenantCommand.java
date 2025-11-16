/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.tenants.domain.commands;

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
