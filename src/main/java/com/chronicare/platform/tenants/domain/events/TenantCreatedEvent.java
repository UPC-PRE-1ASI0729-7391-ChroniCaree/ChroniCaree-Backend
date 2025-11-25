/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.tenants.domain.events;

import com.chronicare.platform.tenants.domain.valueobjects.TenantName;
import org.springframework.context.ApplicationEvent;

/**
 * Evento que indica que un Tenant fue creado.
 */
public class TenantCreatedEvent extends ApplicationEvent {

    private final Long tenantId;
    private final TenantName name;

    public TenantCreatedEvent(Object source, Long tenantId, TenantName name) {
        super(source);
        this.tenantId = tenantId;
        this.name = name;
    }

    public Long tenantId() {
        return tenantId;
    }

    public TenantName name() {
        return name;
    }
}
