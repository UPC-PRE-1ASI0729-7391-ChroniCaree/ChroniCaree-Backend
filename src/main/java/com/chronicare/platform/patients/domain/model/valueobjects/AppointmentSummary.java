package com.chronicare.platform.patients.domain.model.valueobjects;

import java.time.LocalDateTime;

/**
 * Appointment Summary Value Object
 * Represents a summary of an upcoming appointment for patient dashboard
 */
public record AppointmentSummary(
    Long appointmentId,
    Long doctorId,
    String doctorName,
    String specialty,
    LocalDateTime appointmentDateTime,
    String status,
    String appointmentType,
    String location,
    String notes
) {
    public AppointmentSummary {
        if (appointmentId == null) throw new IllegalArgumentException("Appointment ID cannot be null");
        if (doctorId == null) throw new IllegalArgumentException("Doctor ID cannot be null");
        if (doctorName == null || doctorName.isBlank()) throw new IllegalArgumentException("Doctor name cannot be null or blank");
        if (appointmentDateTime == null) throw new IllegalArgumentException("Appointment date time cannot be null");
        if (status == null || status.isBlank()) throw new IllegalArgumentException("Status cannot be null or blank");
    }
    
    public boolean isPending() {
        return "SCHEDULED".equalsIgnoreCase(status) || "CONFIRMED".equalsIgnoreCase(status);
    }
    
    public boolean isToday() {
        LocalDateTime now = LocalDateTime.now();
        return appointmentDateTime.toLocalDate().equals(now.toLocalDate());
    }
    
    public boolean isUpcoming() {
        return appointmentDateTime.isAfter(LocalDateTime.now());
    }
}
