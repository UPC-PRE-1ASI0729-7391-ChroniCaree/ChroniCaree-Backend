package com.chronicare.platform.doctors.domain.model.queries;

/**
 * Query to get doctor dashboard data
 */
public record GetDoctorDashboardQuery(Long doctorId) {
    public GetDoctorDashboardQuery {
        if (doctorId == null) throw new IllegalArgumentException("Doctor ID cannot be null");
    }
}
