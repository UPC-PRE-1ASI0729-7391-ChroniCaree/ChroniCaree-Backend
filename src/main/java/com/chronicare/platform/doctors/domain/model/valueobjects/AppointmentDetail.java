package com.chronicare.platform.doctors.domain.model.valueobjects;

import java.time.LocalDateTime;

/**
 * Summary: Appointment Detail Value Object
 * Represents a detailed appointment for doctor dashboard
 */
public record AppointmentDetail(
    Long appointmentId,
    Long patientId,
    String patientName,
    Integer patientAge,
    LocalDateTime appointmentDateTime,
    String status,
    String appointmentType,
    String reason,
    String location,
    String notes,
    Boolean isUrgent
) {
    public AppointmentDetail {
        if (appointmentId == null) throw new IllegalArgumentException("Appointment ID cannot be null");
        if (patientId == null) throw new IllegalArgumentException("Patient ID cannot be null");
        if (patientName == null || patientName.isBlank()) throw new IllegalArgumentException("Patient name cannot be null or blank");
        if (appointmentDateTime == null) throw new IllegalArgumentException("Appointment date time cannot be null");
        if (status == null || status.isBlank()) throw new IllegalArgumentException("Status cannot be null or blank");
    }
    
    public boolean isScheduled() {
        return "SCHEDULED".equalsIgnoreCase(status);
    }
    
    public boolean isConfirmed() {
        return "CONFIRMED".equalsIgnoreCase(status);
    }
    
    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status);
    }
    
    public boolean isPending() {
        return isScheduled() || isConfirmed();
    }
    
    public boolean isToday() {
        LocalDateTime now = LocalDateTime.now();
        return appointmentDateTime.toLocalDate().equals(now.toLocalDate());
    }
}
