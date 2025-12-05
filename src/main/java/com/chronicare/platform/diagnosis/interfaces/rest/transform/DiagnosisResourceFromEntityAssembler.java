package com.chronicare.platform.diagnosis.interfaces.rest.transform;

import com.chronicare.platform.diagnosis.domain.model.aggregates.Diagnosis;
import com.chronicare.platform.diagnosis.interfaces.rest.resources.DiagnosisResource;

/**
 * Assembler to convert Diagnosis entity to DiagnosisResource
 */
public class DiagnosisResourceFromEntityAssembler {

    public static DiagnosisResource toResourceFromEntity(Diagnosis entity) {
        return new DiagnosisResource(
                entity.getId(),
                entity.getPatientId(),
                entity.getDoctorId(),
                entity.getIcd10Code(),
                entity.getDiagnosisName(),
                entity.getStatus(),
                entity.getSeverity(),
                entity.getDiagnosedDate(),
                entity.getResolvedDate(),
                entity.getNotes(),
                entity.getTreatment(),
                entity.isFollowUpRequired(),
                entity.getLastReviewDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
