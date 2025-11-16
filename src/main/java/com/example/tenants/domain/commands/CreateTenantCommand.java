/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.tenants.domain.commands;


import com.example.tenants.domain.valueobjects.TenantName;

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