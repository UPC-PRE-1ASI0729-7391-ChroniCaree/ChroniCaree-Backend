/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.tenants.domain.queries;

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
