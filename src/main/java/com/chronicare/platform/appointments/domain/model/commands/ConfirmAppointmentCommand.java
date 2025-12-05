package com.chronicare.platform.appointments.domain.model.commands;

/**
 * Command to confirm an appointment
 */
public record ConfirmAppointmentCommand(Long appointmentId) {
    public ConfirmAppointmentCommand {
        if (appointmentId == null) throw new IllegalArgumentException("Appointment ID cannot be null");
    }
}
