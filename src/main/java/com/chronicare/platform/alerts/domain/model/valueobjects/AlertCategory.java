package com.chronicare.platform.alerts.domain.model.valueobjects;

/**
 * Summary: Alert Category Value Object
 * Defines the categories of alerts
 */
public enum AlertCategory {
    CLINICAL("clinical", "Relacionado con salud del paciente"),
    ADMINISTRATIVE("administrative", "Relacionado con gestión"),
    OPERATIONAL("operational", "Relacionado con operaciones del hospital"),
    FINANCIAL("financial", "Relacionado con pagos/subscripciones"),
    TECHNICAL("technical", "Relacionado con sistema");

    private final String code;
    private final String description;

    AlertCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AlertCategory fromCode(String code) {
        if (code == null) return null;
        for (AlertCategory category : values()) {
            if (category.code.equalsIgnoreCase(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown alert category: " + code);
    }
}
