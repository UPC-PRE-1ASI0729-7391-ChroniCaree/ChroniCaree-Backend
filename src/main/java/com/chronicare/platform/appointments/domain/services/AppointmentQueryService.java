package com.chronicare.platform.appointments.domain.services;

import com.chronicare.platform.appointments.domain.model.aggregates.Appointment;
import com.chronicare.platform.appointments.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Appointment Query Service
 * Handles appointment read operations
 */
public interface AppointmentQueryService {
    Optional<Appointment> handle(GetAppointmentByIdQuery query);
    List<Appointment> handle(GetAppointmentsByPatientQuery query);
    List<Appointment> handle(GetAppointmentsByDoctorAndDateQuery query);
}
