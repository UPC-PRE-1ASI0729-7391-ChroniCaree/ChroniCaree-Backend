package com.chronicare.platform.diagnosis.domain.model.queries;

/**
 * Query to get diagnoses by patient ID
 */
public record GetDiagnosesByPatientIdQuery(Long patientId) {
}
