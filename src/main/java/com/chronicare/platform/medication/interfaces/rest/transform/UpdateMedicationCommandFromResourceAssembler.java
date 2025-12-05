package com.chronicare.platform.medication.interfaces.rest.transform;

import com.chronicare.platform.medication.domain.model.commands.UpdateMedicationCommand;
import com.chronicare.platform.medication.interfaces.rest.resources.UpdateMedicationResource;

/**
 * Assembler to convert UpdateMedicationResource to UpdateMedicationCommand
 */
public class UpdateMedicationCommandFromResourceAssembler {

    public static UpdateMedicationCommand toCommandFromResource(Long id, UpdateMedicationResource resource) {
        return new UpdateMedicationCommand(
                id,
                resource.name(),
                resource.type(),
                resource.dosage(),
                resource.frequency(),
                resource.timeOfDay(),
                resource.status(),
                resource.instructions(),
                resource.sideEffects(),
                resource.contraindications(),
                resource.purpose(),
                resource.refillDate()
        );
    }
}
