package com.chronicare.platform.alerts.domain.model.queries;

/**
 * Summary: Query to get alerts by doctor ID with optional filters
 */
public record GetAlertsByDoctorIdQuery(
    Long doctorId,
    String status,
    String severity,
    String category,
    Long patientId,
    Integer page,
    Integer limit,
    String sortBy
) {
    public GetAlertsByDoctorIdQuery {
        if (doctorId == null) {
            throw new IllegalArgumentException("doctorId is required");
        }
    }

    public GetAlertsByDoctorIdQuery(Long doctorId) {
        this(doctorId, null, null, null, null, 0, 20, "createdAt DESC");
    }
}
