package com.chronicare.platform.alerts.domain.model.events;

import com.chronicare.platform.shared.domain.DomainEvent;

import java.time.LocalDateTime;

/**
 * Domain event fired when an alert status changes
 */
public class AlertStatusChangedEvent extends DomainEvent {
    
    private final Long alertId;
    private final Long patientId;
    private final Long tenantId;
    private final String previousStatus;
    private final String newStatus;
    private final Long changedBy;

    public AlertStatusChangedEvent(Long alertId, Long patientId, Long tenantId, 
                                  String previousStatus, String newStatus, Long changedBy) {
        this.alertId = alertId;
        this.patientId = patientId;
        this.tenantId = tenantId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
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

    public String getPreviousStatus() {
        return previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public Long getChangedBy() {
        return changedBy;
    }

    public String eventType() {
        return "alert.status.changed";
    }
}
