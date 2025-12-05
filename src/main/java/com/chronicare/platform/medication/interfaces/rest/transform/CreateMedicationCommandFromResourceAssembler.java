package com.chronicare.platform.medication.interfaces.rest.transform;

import com.chronicare.platform.medication.domain.model.commands.CreateMedicationCommand;
import com.chronicare.platform.medication.interfaces.rest.resources.CreateMedicationResource;

/**
 * Assembler to convert CreateMedicationResource to CreateMedicationCommand
 */
public class CreateMedicationCommandFromResourceAssembler {

    public static CreateMedicationCommand toCommandFromResource(CreateMedicationResource resource) {
        return new CreateMedicationCommand(
                resource.patientId(),
                resource.name(),
                resource.type(),
                resource.dosage(),
                resource.frequency(),
                resource.timeOfDay(),
                resource.prescribedBy(),
                resource.prescribedDate(),
                resource.status(),
                resource.instructions(),
                resource.sideEffects(),
                resource.contraindications(),
                resource.purpose(),
                resource.refillDate()
        );
    }
}
