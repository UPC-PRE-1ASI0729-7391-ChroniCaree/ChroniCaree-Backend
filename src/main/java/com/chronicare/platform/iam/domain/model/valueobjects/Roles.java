package com.chronicare.platform.iam.domain.model.valueobjects;

/**
 * Roles value object representing user roles in the system
 * @summary Defines the three main roles: PATIENT, DOCTOR, HOSPITAL_ADMIN
 */
public enum Roles {
    PATIENT("patient"),
    DOCTOR("doctor"),
    HOSPITAL_ADMIN("hospital_admin");

    private final String name;

    Roles(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Get role from string name
     */
    public static Roles fromName(String name) {
        for (Roles role : Roles.values()) {
            if (role.getName().equalsIgnoreCase(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role name: " + name);
    }
}
