package com.chronicare.platform.appointments.domain.model.queries;

/**
 * Query to get appointment by ID
 */
public record GetAppointmentByIdQuery(Long appointmentId) {
    public GetAppointmentByIdQuery {
        if (appointmentId == null) throw new IllegalArgumentException("Appointment ID cannot be null");
    }
}
