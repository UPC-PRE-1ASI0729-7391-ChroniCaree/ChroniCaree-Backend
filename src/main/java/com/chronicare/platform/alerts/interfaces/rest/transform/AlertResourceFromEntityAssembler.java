package com.chronicare.platform.alerts.interfaces.rest.transform;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.interfaces.rest.resources.AlertResource;

import java.time.format.DateTimeFormatter;

/**
 * Assembler to convert Alert entity to AlertResource
 */
public class AlertResourceFromEntityAssembler {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    
    public static AlertResource toResourceFromEntity(Alert alert) {
        return new AlertResource(
            alert.getId(),
            alert.getPatientId(),
            alert.getDoctorId(),
            alert.getTenantId(),
            alert.getType() != null ? alert.getType().name() : null,
            alert.getSeverity() != null ? alert.getSeverity().name() : null,
            alert.getCategory() != null ? alert.getCategory().name() : null,
            alert.getTitle(),
            alert.getMessage(),
            alert.getDescription(),
            alert.getSourceType(),
            alert.getSourceId(),
            alert.getMetadata(),
            alert.getStatus() != null ? alert.getStatus().name() : null,
            alert.getPriority(),
            alert.getAcknowledgedBy(),
            alert.getAcknowledgedAt() != null ? alert.getAcknowledgedAt().format(FORMATTER) : null,
            alert.getAcknowledgedNotes(),
            alert.getResolvedBy(),
            alert.getResolvedAt() != null ? alert.getResolvedAt().format(FORMATTER) : null,
            alert.getResolutionNotes(),
            alert.getResolutionAction(),
            alert.getEscalated(),
            alert.getEscalatedTo(),
            alert.getEscalatedAt() != null ? alert.getEscalatedAt().format(FORMATTER) : null,
            alert.getEscalationLevel(),
            alert.getCreatedAt() != null ? alert.getCreatedAt().format(FORMATTER) : null,
            alert.getUpdatedAt() != null ? alert.getUpdatedAt().format(FORMATTER) : null,
            alert.getExpiresAt() != null ? alert.getExpiresAt().format(FORMATTER) : null
        );
    }
}
