package com.chronicare.platform.diagnosis.domain.model.commands;

import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisSeverity;
import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisStatus;

import java.time.LocalDate;

/**
 * Command to update an existing diagnosis
 */
public record UpdateDiagnosisCommand(
        Long id,
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
