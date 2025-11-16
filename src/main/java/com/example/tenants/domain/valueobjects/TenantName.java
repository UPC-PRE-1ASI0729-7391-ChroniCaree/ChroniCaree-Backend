/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.tenants.domain.valueobjects;

import java.util.Objects;

/**
 * Value Object que envuelve el name del Tenant.
 */
public final class TenantName {

    private final String value;

    public TenantName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tenant name cannot be null or blank");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("Tenant name too long (max 255 chars)");
        }
        this.value = value.trim();
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantName)) return false;
        TenantName that = (TenantName) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
