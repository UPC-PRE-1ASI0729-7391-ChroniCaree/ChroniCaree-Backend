package com.chronicare.platform.diagnosis.interfaces.rest.resources;

import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisSeverity;
import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisStatus;

import java.time.LocalDate;

/**
 * Resource for creating a new Diagnosis
 */
public record CreateDiagnosisResource(
        Long patientId,
        Long doctorId,
        String icd10Code,
        String diagnosisName,
        DiagnosisStatus status,
        DiagnosisSeverity severity,
        LocalDate diagnosedDate,
        LocalDate resolvedDate,
        String notes,
        String treatment,
        boolean followUpRequired,
        LocalDate lastReviewDate
) {
}
