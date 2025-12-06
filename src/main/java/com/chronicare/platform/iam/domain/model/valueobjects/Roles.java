package com.chronicare.platform.iam.domain.model.valueobjects;

/**
 * Roles value object representing user roles in the system
 * @summary Defines the three main roles: PATIENT, DOCTOR, HOSPITAL_ADMIN
 */
public enum Roles {
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR"),
    HOSPITAL_ADMIN("HOSPITAL_ADMIN"),
    TENANT_ADMIN("TENANT_ADMIN");

    private final String name;

    Roles(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Get role from string name
     * Accepts both TENANT_ADMIN and HOSPITAL_ADMIN for backward compatibility
     */
    public static Roles fromName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Role name cannot be null");
        }
        
        // Normalize: hospital_admin -> HOSPITAL_ADMIN, tenant_admin -> TENANT_ADMIN
        String normalized = name.toUpperCase().replace("_", "_");
        
        // Map TENANT_ADMIN to HOSPITAL_ADMIN for backward compatibility
        if ("TENANT_ADMIN".equals(normalized)) {
            return HOSPITAL_ADMIN;
        }
        
        for (Roles role : Roles.values()) {
            if (role.getName().equalsIgnoreCase(name) || role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role name: " + name);
    }
}
