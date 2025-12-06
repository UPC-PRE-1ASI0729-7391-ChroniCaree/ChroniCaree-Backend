package com.chronicare.platform.alerts.domain.model.valueobjects;

/**
 * Summary: Alert Status Value Object
 * Defines the possible states of an alert
 */
public enum AlertStatus {
    ACTIVE("ACTIVE", "Alerta activa, pendiente de atención"),
    ACKNOWLEDGED("ACKNOWLEDGED", "Alerta reconocida, en proceso"),
    RESOLVED("RESOLVED", "Alerta resuelta"),
    DISMISSED("DISMISSED", "Alerta descartada"),
    EXPIRED("EXPIRED", "Alerta expirada"),
    ESCALATED("ESCALATED", "Alerta escalada");

    private final String code;
    private final String description;

    AlertStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AlertStatus fromCode(String code) {
        for (AlertStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown alert status: " + code);
    }

    public boolean canTransitionTo(AlertStatus target) {
        return switch (this) {
            case ACTIVE -> target == ACKNOWLEDGED || target == RESOLVED || target == DISMISSED || target == ESCALATED || target == EXPIRED;
            case ACKNOWLEDGED -> target == RESOLVED || target == ESCALATED;
            case ESCALATED -> target == RESOLVED || target == DISMISSED;
            case RESOLVED, DISMISSED, EXPIRED -> false;
        };
    }
}
