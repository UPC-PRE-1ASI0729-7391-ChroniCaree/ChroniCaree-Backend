package com.chronicare.platform.appointments.domain.model.queries;

import java.time.LocalDate;

/**
 * Query to get appointments by doctor and date
 */
public record GetAppointmentsByDoctorAndDateQuery(
    Long doctorId,
    LocalDate date
) {
    public GetAppointmentsByDoctorAndDateQuery {
        if (doctorId == null) throw new IllegalArgumentException("Doctor ID cannot be null");
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
    }
}
