package com.chronicare.platform.medication.domain.model.queries;

/**
 * Query to get medications by patient ID
 */
public record GetMedicationsByPatientIdQuery(String patientId) {
}
