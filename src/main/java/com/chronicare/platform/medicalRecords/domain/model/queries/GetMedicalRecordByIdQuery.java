package com.chronicare.platform.medicalRecords.domain.model.queries;

/**
 * Query to get a medical record by id
 *
 * @param medicalRecordId The ID of the medical record to retrieve
 */
public record GetMedicalRecordByIdQuery(Long medicalRecordId) {
    public GetMedicalRecordByIdQuery {
        if (medicalRecordId == null) {
            throw new IllegalArgumentException("Medical record ID cannot be null");
        }
    }
}

