package com.chronicare.platform.patients.domain.model.valueobjects;

import java.time.LocalDateTime;

/**
 * Medication Summary Value Object
 * Represents a summary of active medication for patient dashboard
 */
public record MedicationSummary(
    Long medicationId,
    String medicationName,
    String dosage,
    String frequency,
    LocalDateTime startDate,
    LocalDateTime endDate,
    String prescribedBy,
    String instructions,
    Boolean isActive
) {
    public MedicationSummary {
        if (medicationId == null) throw new IllegalArgumentException("Medication ID cannot be null");
        if (medicationName == null || medicationName.isBlank()) throw new IllegalArgumentException("Medication name cannot be null or blank");
        if (dosage == null || dosage.isBlank()) throw new IllegalArgumentException("Dosage cannot be null or blank");
        if (frequency == null || frequency.isBlank()) throw new IllegalArgumentException("Frequency cannot be null or blank");
        if (isActive == null) throw new IllegalArgumentException("Active status cannot be null");
    }
    
    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        boolean afterStart = startDate == null || now.isAfter(startDate) || now.isEqual(startDate);
        boolean beforeEnd = endDate == null || now.isBefore(endDate);
        return isActive && afterStart && beforeEnd;
    }
    
    public boolean isExpiringSoon() {
        if (endDate == null) return false;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysFromNow = now.plusDays(7);
        return endDate.isBefore(sevenDaysFromNow) && endDate.isAfter(now);
    }
}
