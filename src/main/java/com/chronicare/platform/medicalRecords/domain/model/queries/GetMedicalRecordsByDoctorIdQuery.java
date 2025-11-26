package com.chronicare.platform.medicalRecords.domain.model.queries;

/**
 * Query to get medical records by doctor id
 *
 * @param doctorId The ID of the doctor
 */
public record GetMedicalRecordsByDoctorIdQuery(Long doctorId) {
    public GetMedicalRecordsByDoctorIdQuery {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }
    }
}

