package com.chronicare.platform.diagnosis.interfaces.rest.transform;

import com.chronicare.platform.diagnosis.domain.model.commands.UpdateDiagnosisCommand;
import com.chronicare.platform.diagnosis.interfaces.rest.resources.UpdateDiagnosisResource;

/**
 * Summary: Assembler to convert UpdateDiagnosisResource to UpdateDiagnosisCommand
 */
public class UpdateDiagnosisCommandFromResourceAssembler {

    public static UpdateDiagnosisCommand toCommandFromResource(Long id, UpdateDiagnosisResource resource) {
        return new UpdateDiagnosisCommand(
                id,
                resource.icd10Code(),
                resource.diagnosisName(),
                resource.status(),
                resource.severity(),
                resource.resolvedDate(),
                resource.notes(),
                resource.treatment(),
                resource.followUpRequired(),
                resource.lastReviewDate()
        );
    }
}
