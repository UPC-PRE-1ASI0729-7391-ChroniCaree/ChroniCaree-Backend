package com.chronicare.platform.alerts.domain.model.events;

import com.chronicare.platform.shared.domain.DomainEvent;

import java.time.LocalDateTime;

/**
 * Domain event fired when an alert is assigned to a user
 */
public class AlertAssignedEvent extends DomainEvent {
    
    private final Long alertId;
    private final Long patientId;
    private final Long tenantId;
    private final Long assignedToUserId;
    private final String assignedRole;

    public AlertAssignedEvent(Long alertId, Long patientId, Long tenantId, 
                            Long assignedToUserId, String assignedRole) {
        this.alertId = alertId;
        this.patientId = patientId;
        this.tenantId = tenantId;
        this.assignedToUserId = assignedToUserId;
        this.assignedRole = assignedRole;
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

    public Long getAssignedToUserId() {
        return assignedToUserId;
    }

    public String getAssignedRole() {
        return assignedRole;
    }

    public String eventType() {
        return "alert.assigned";
    }
}
