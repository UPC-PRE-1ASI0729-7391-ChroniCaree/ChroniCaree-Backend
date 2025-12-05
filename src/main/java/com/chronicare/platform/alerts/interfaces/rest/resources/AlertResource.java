package com.chronicare.platform.alerts.interfaces.rest.resources;

/**
 * Resource for alert response
 */
public record AlertResource(
    Long id,
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
    String status,
    Integer priority,
    Long acknowledgedBy,
    String acknowledgedAt,
    String acknowledgedNotes,
    Long resolvedBy,
    String resolvedAt,
    String resolutionNotes,
    String resolutionAction,
    Boolean escalated,
    Long escalatedTo,
    String escalatedAt,
    Integer escalationLevel,
    String createdAt,
    String updatedAt,
    String expiresAt
) {}
