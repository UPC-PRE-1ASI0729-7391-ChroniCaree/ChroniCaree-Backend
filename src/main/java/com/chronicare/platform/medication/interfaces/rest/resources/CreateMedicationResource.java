package com.chronicare.platform.medication.interfaces.rest.resources;

import com.chronicare.platform.medication.domain.model.valueobjects.MedicationFrequency;
import com.chronicare.platform.medication.domain.model.valueobjects.MedicationStatus;
import com.chronicare.platform.medication.domain.model.valueobjects.MedicationType;

import java.time.LocalDate;
import java.util.List;

/**
 * Resource for creating a new Medication
 */
public record CreateMedicationResource(
        String patientId,
        String name,
        MedicationType type,
        String dosage,
        MedicationFrequency frequency,
        String timeOfDay,
        String prescribedBy,
        LocalDate prescribedDate,
        MedicationStatus status,
        String instructions,
        List<String> sideEffects,
        List<String> contraindications,
        String purpose,
        LocalDate refillDate
) {
}
