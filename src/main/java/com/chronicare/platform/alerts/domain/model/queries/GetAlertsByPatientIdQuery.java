package com.chronicare.platform.alerts.domain.model.queries;

/**
 * Summary: Query to get alerts by patient ID with optional filters
 */
public record GetAlertsByPatientIdQuery(
    Long patientId,
    String status,
    String severity,
    String category,
    Integer page,
    Integer limit
) {
    public GetAlertsByPatientIdQuery {
        if (patientId == null) {
            throw new IllegalArgumentException("patientId is required");
        }
    }

    public GetAlertsByPatientIdQuery(Long patientId) {
        this(patientId, null, null, null, 0, 20);
    }
}
