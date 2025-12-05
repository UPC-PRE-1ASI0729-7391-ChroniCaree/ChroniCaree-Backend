package com.chronicare.platform.medication.domain.model.commands;

import com.chronicare.platform.medication.domain.model.valueobjects.MedicationFrequency;
import com.chronicare.platform.medication.domain.model.valueobjects.MedicationStatus;
import com.chronicare.platform.medication.domain.model.valueobjects.MedicationType;

import java.time.LocalDate;
import java.util.List;

/**
 * Command to update an existing medication
 */
public record UpdateMedicationCommand(
        Long id,
        String name,
        MedicationType type,
        String dosage,
        MedicationFrequency frequency,
        String timeOfDay,
        MedicationStatus status,
        String instructions,
        List<String> sideEffects,
        List<String> contraindications,
        String purpose,
        LocalDate refillDate
) {
}
