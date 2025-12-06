package com.chronicare.platform.alerts.domain.model.valueobjects;

/**
 * Summary: Alert Type Value Object
 * Defines all possible types of alerts in the system
 */
public enum AlertType {
    // Vital Signs
    VITAL_SIGN_ABNORMAL("vital_sign_abnormal", "Signo vital fuera de rango"),
    VITAL_SIGN_CRITICAL("vital_sign_critical", "Signo vital crítico"),
    
    // Medication
    MEDICATION_MISSED("medication_missed", "Medicación no tomada"),
    MEDICATION_INTERACTION("medication_interaction", "Interacción de medicamentos"),
    
    // Appointments
    APPOINTMENT_REMINDER("appointment_reminder", "Recordatorio de cita"),
    APPOINTMENT_MISSED("appointment_missed", "Cita perdida"),
    
    // Symptoms
    SYMPTOM_WORSENING("symptom_worsening", "Síntoma empeorando"),
    SYMPTOM_CRITICAL("symptom_critical", "Síntoma crítico"),
    
    // Lab Results
    LAB_RESULT_ABNORMAL("lab_result_abnormal", "Resultado de laboratorio anormal"),
    
    // Treatment
    TREATMENT_OVERDUE("treatment_overdue", "Tratamiento vencido"),
    FOLLOWUP_REQUIRED("followup_required", "Seguimiento requerido"),
    
    // System
    SYSTEM_NOTIFICATION("system_notification", "Notificación del sistema");

    private final String code;
    private final String description;

    AlertType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AlertType fromCode(String code) {
        for (AlertType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown alert type: " + code);
    }
}
