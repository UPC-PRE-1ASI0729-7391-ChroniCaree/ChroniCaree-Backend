package com.chronicare.platform.alerts.interfaces.rest.resources;

/**
 * Resource for creating an alert
 */
public record CreateAlertResource(
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
) {}
