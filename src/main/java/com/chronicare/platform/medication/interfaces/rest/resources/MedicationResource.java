package com.chronicare.platform.medication.interfaces.rest.resources;

import com.chronicare.platform.medication.domain.model.valueobjects.MedicationStatus;
import com.chronicare.platform.medication.domain.model.valueobjects.MedicationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Resource representing a Medication with schedule details
 * Matches frontend expected structure with nested schedule object
 */
public record MedicationResource(
        Long id,
        Long patientId,
        String name,
        MedicationType type,
        String dosage,
        MedicationScheduleResource schedule,  // Nested schedule object containing frequency, times, dates
        String prescribedBy,
        LocalDate prescribedDate,
        MedicationStatus status,
        String instructions,
        List<String> sideEffects,
        List<String> contraindications,
        String purpose,
        LocalDate refillDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
