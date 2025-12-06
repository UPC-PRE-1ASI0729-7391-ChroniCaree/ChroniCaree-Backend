package com.chronicare.platform.diagnosis.interfaces.rest.transform;

import com.chronicare.platform.diagnosis.domain.model.commands.CreateDiagnosisCommand;
import com.chronicare.platform.diagnosis.interfaces.rest.resources.CreateDiagnosisResource;

/**
 * Summary: Assembler to convert CreateDiagnosisResource to CreateDiagnosisCommand
 */
public class CreateDiagnosisCommandFromResourceAssembler {

    public static CreateDiagnosisCommand toCommandFromResource(CreateDiagnosisResource resource) {
        return new CreateDiagnosisCommand(
                resource.patientId(),
                resource.doctorId(),
                resource.icd10Code(),
                resource.diagnosisName(),
                resource.status(),
                resource.severity(),
                resource.diagnosedDate(),
                resource.resolvedDate(),
                resource.notes(),
                resource.treatment(),
                resource.followUpRequired(),
                resource.lastReviewDate()
        );
    }
}
