package com.chronicare.platform.appointments.domain.model.commands;

/**
 * Summary: Command to complete an appointment
 */
public record CompleteAppointmentCommand(
    Long appointmentId,
    String completionNotes
) {
    public CompleteAppointmentCommand {
        if (appointmentId == null) throw new IllegalArgumentException("Appointment ID cannot be null");
    }
}
