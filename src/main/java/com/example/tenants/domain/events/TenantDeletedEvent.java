/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.tenants.domain.events;

import org.springframework.context.ApplicationEvent;

/**
 * Evento que indica que un Tenant fue eliminado.
 */
public class TenantDeletedEvent extends ApplicationEvent {

    private final Long tenantId;

    public TenantDeletedEvent(Object source, Long tenantId) {
        super(source);
        this.tenantId = tenantId;
    }

    public Long tenantId() {
        return tenantId;
    }
}