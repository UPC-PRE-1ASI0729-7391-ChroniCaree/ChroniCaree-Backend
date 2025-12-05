package com.chronicare.platform.patients.domain.model.queries;

/**
 * Query to get patient dashboard data
 */
public record GetPatientDashboardQuery(Long patientId) {
    public GetPatientDashboardQuery {
        if (patientId == null) throw new IllegalArgumentException("Patient ID cannot be null");
    }
}
