package com.chronicare.platform.appointments.domain.model.commands;

/**
 * Command to cancel an appointment
 */
public record CancelAppointmentCommand(
    Long appointmentId,
    String cancellationReason
) {
    public CancelAppointmentCommand {
        if (appointmentId == null) throw new IllegalArgumentException("Appointment ID cannot be null");
    }
}
