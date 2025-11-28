package com.chronicare.platform.medicalRecords.domain.model.queries;

/**
 * Query to get medical records by patient id
 *
 * @param patientId The ID of the patient
 */
public record GetMedicalRecordsByPatientIdQuery(Long patientId) {
    public GetMedicalRecordsByPatientIdQuery {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
    }
}

