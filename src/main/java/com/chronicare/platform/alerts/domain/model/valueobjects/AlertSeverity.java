package com.chronicare.platform.alerts.domain.model.valueobjects;

/**
 * Alert Severity Value Object
 * Defines the severity levels of alerts
 */
public enum AlertSeverity {
    LOW("low", "Baja prioridad, informativo", 1),
    MEDIUM("medium", "Prioridad media, requiere atención", 2),
    HIGH("high", "Alta prioridad, requiere atención pronta", 3),
    CRITICAL("critical", "Crítico, requiere atención inmediata", 4);

    private final String code;
    private final String description;
    private final int priority;

    AlertSeverity(String code, String description, int priority) {
        this.code = code;
        this.description = description;
        this.priority = priority;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public static AlertSeverity fromCode(String code) {
        if (code == null) return null;
        for (AlertSeverity severity : values()) {
            if (severity.code.equalsIgnoreCase(code) || severity.name().equalsIgnoreCase(code)) {
                return severity;
            }
        }
        throw new IllegalArgumentException("Unknown alert severity: " + code);
    }
}
