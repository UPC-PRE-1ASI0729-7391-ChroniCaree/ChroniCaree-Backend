package com.chronicare.platform.appointments.domain.model.commands;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Summary: Command to create a new appointment
 */
public record CreateAppointmentCommand(
    Long patientId,
    Long doctorId,
    LocalDate appointmentDate,
    LocalTime appointmentTime,
    String appointmentType,
    String reason,
    String location,
    String notes,
    Integer duration
) {
    public CreateAppointmentCommand {
        if (patientId == null) throw new IllegalArgumentException("Patient ID cannot be null");
        if (doctorId == null) throw new IllegalArgumentException("Doctor ID cannot be null");
        if (appointmentDate == null) throw new IllegalArgumentException("Appointment date cannot be null");
        if (appointmentTime == null) throw new IllegalArgumentException("Appointment time cannot be null");
        if (appointmentType == null || appointmentType.isBlank()) throw new IllegalArgumentException("Appointment type cannot be null or blank");
    }
}
