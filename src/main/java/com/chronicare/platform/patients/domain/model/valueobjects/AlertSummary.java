package com.chronicare.platform.patients.domain.model.valueobjects;

import java.time.LocalDateTime;

/**
 * Alert Summary Value Object
 * Represents an active alert for patient dashboard
 */
public record AlertSummary(
    Long alertId,
    String alertType,
    String severity,
    String message,
    LocalDateTime triggeredAt,
    Boolean isRead,
    String actionRequired,
    LocalDateTime dismissedAt
) {
    public AlertSummary {
        if (alertId == null) throw new IllegalArgumentException("Alert ID cannot be null");
        if (alertType == null || alertType.isBlank()) throw new IllegalArgumentException("Alert type cannot be null or blank");
        if (severity == null || severity.isBlank()) throw new IllegalArgumentException("Severity cannot be null or blank");
        if (message == null || message.isBlank()) throw new IllegalArgumentException("Message cannot be null or blank");
        if (triggeredAt == null) throw new IllegalArgumentException("Triggered at cannot be null");
        if (isRead == null) throw new IllegalArgumentException("Read status cannot be null");
    }
    
    public boolean isCritical() {
        return "CRITICAL".equalsIgnoreCase(severity);
    }
    
    public boolean isHigh() {
        return "HIGH".equalsIgnoreCase(severity);
    }
    
    public boolean requiresAction() {
        return actionRequired != null && !actionRequired.isBlank();
    }
    
    public boolean isDismissed() {
        return dismissedAt != null;
    }
    
    public boolean isActive() {
        return !isRead && dismissedAt == null;
    }
}
