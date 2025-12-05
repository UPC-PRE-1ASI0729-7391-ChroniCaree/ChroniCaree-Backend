package com.chronicare.platform.appointments.domain.model.commands;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command to update an existing appointment
 */
public record UpdateAppointmentCommand(
    Long appointmentId,
    LocalDate appointmentDate,
    LocalTime appointmentTime,
    String appointmentType,
    String reason,
    String location,
    String notes,
    Integer duration
) {
    public UpdateAppointmentCommand {
        if (appointmentId == null) throw new IllegalArgumentException("Appointment ID cannot be null");
    }
}
