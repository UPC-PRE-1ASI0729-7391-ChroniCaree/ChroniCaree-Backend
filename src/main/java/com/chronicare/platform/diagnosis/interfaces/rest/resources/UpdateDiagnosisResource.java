package com.chronicare.platform.diagnosis.interfaces.rest.resources;

import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisSeverity;
import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisStatus;

import java.time.LocalDate;

/**
 * Summary: Resource for updating an existing Diagnosis
 */
public record UpdateDiagnosisResource(
        String icd10Code,
        String diagnosisName,
        DiagnosisStatus status,
        DiagnosisSeverity severity,
        LocalDate resolvedDate,
        String notes,
        String treatment,
        boolean followUpRequired,
        LocalDate lastReviewDate
) {
}
