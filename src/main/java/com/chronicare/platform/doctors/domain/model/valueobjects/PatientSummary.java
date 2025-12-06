package com.chronicare.platform.doctors.domain.model.valueobjects;

import java.time.LocalDateTime;

/**
 * Summary: Patient Summary Value Object
 * Represents a patient summary for doctor dashboard
 */
public record PatientSummary(
    Long patientId,
    String patientName,
    Integer age,
    String gender,
    String bloodType,
    String contactPhone,
    LocalDateTime lastVisit,
    Integer totalVisits,
    Boolean hasActiveAlerts,
    Integer criticalAlerts,
    String currentCondition
) {
    public PatientSummary {
        if (patientId == null) throw new IllegalArgumentException("Patient ID cannot be null");
        if (patientName == null || patientName.isBlank()) throw new IllegalArgumentException("Patient name cannot be null or blank");
    }
    
    public boolean hasRecentVisit() {
        if (lastVisit == null) return false;
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return lastVisit.isAfter(oneMonthAgo);
    }
    
    public boolean hasCriticalAlerts() {
        return criticalAlerts != null && criticalAlerts > 0;
    }
    
    public boolean isNewPatient() {
        return totalVisits != null && totalVisits <= 1;
    }
}
