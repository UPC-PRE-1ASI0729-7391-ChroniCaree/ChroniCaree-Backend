package com.chronicare.platform.diagnosis.domain.model.queries;

import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisStatus;

/**
 * Query to get diagnoses by status
 */
public record GetDiagnosesByStatusQuery(DiagnosisStatus status) {
}
