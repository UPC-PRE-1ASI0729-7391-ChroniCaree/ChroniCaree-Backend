package com.chronicare.platform.alerts.domain.model.events;

import com.chronicare.platform.shared.domain.DomainEvent;

import java.time.LocalDateTime;

/**
 * Domain event fired when an alert is created
 */
public class AlertCreatedEvent extends DomainEvent {
    
    private final Long alertId;
    private final Long patientId;
    private final Long tenantId;
    private final String type;
    private final String severity;
    private final String source;
    private final LocalDateTime detectedAt;

    public AlertCreatedEvent(Long alertId, Long patientId, Long tenantId, String type, 
                           String severity, String source, LocalDateTime detectedAt) {
        this.alertId = alertId;
        this.patientId = patientId;
        this.tenantId = tenantId;
        this.type = type;
        this.severity = severity;
        this.source = source;
        this.detectedAt = detectedAt;
    }

    public Long getAlertId() {
        return alertId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getType() {
        return type;
    }

    public String getSeverity() {
        return severity;
    }

    public String getSource() {
        return source;
    }

    public LocalDateTime getDetectedAt() {
        return detectedAt;
    }

    public String eventType() {
        return "alert.created";
    }
}
