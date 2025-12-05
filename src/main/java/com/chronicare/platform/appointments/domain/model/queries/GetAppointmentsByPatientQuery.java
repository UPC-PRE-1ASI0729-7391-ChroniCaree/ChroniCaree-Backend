package com.chronicare.platform.appointments.domain.model.queries;

/**
 * Query to get appointments by patient
 */
public record GetAppointmentsByPatientQuery(Long patientId) {
    public GetAppointmentsByPatientQuery {
        if (patientId == null) throw new IllegalArgumentException("Patient ID cannot be null");
    }
}
