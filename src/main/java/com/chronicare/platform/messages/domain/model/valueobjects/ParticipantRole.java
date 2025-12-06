package com.chronicare.platform.messages.domain.model.valueobjects;

public enum ParticipantRole {
    PATIENT("PATIENT", "Patient role"),
    DOCTOR("DOCTOR", "Doctor role"),
    TENANT_ADMIN("TENANT_ADMIN", "Tenant admin role"),
    SYSTEM("SYSTEM", "System role");

    private final String code;
    private final String description;

    ParticipantRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ParticipantRole fromCode(String code) {
        if (code == null) return null;
        for (ParticipantRole role : values()) {
            if (role.code.equalsIgnoreCase(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown participant role: " + code);
    }
}
