package com.chronicare.platform.diagnosis.domain.model.commands;

import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisSeverity;
import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisStatus;

import java.time.LocalDate;

/**
 * Command to create a new diagnosis
 */
public record CreateDiagnosisCommand(
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
