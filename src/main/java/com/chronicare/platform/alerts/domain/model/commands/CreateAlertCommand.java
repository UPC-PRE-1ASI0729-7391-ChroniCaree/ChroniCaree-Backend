package com.chronicare.platform.alerts.domain.model.commands;

/**
 * Command to create a new alert
 */
public record CreateAlertCommand(
    Long patientId,
    Long doctorId,
    Long tenantId,
    String type,
    String severity,
    String category,
    String title,
    String message,
    String description,
    String sourceType,
    Long sourceId,
    String metadata,
    Integer priority,
    String expiresAt
) {
    public CreateAlertCommand {
        if (patientId == null) {
            throw new IllegalArgumentException("patientId is required");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("type is required");
        }
        if (severity == null || severity.isBlank()) {
            throw new IllegalArgumentException("severity is required");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
    }
}
