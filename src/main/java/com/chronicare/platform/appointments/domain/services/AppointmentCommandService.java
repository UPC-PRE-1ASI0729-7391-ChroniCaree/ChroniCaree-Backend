package com.chronicare.platform.appointments.domain.services;

import com.chronicare.platform.appointments.domain.model.aggregates.Appointment;
import com.chronicare.platform.appointments.domain.model.commands.*;

import java.util.Optional;

/**
 * Appointment Command Service
 * Handles appointment write operations
 */
public interface AppointmentCommandService {
    Optional<Appointment> handle(CreateAppointmentCommand command);
    Optional<Appointment> handle(UpdateAppointmentCommand command);
    Optional<Appointment> handle(ConfirmAppointmentCommand command);
    Optional<Appointment> handle(CompleteAppointmentCommand command);
    Optional<Appointment> handle(CancelAppointmentCommand command);
}
