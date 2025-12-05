package com.chronicare.platform.medication.domain.services;

import com.chronicare.platform.medication.domain.model.aggregates.Medication;
import com.chronicare.platform.medication.domain.model.commands.CreateMedicationCommand;
import com.chronicare.platform.medication.domain.model.commands.DeleteMedicationCommand;
import com.chronicare.platform.medication.domain.model.commands.UpdateMedicationCommand;

import java.util.Optional;

/**
 * Medication Command Service Interface
 * Handles commands for medication operations
 */
public interface MedicationCommandService {
    Optional<Medication> handle(CreateMedicationCommand command);
    Optional<Medication> handle(UpdateMedicationCommand command);
    void handle(DeleteMedicationCommand command);
}
