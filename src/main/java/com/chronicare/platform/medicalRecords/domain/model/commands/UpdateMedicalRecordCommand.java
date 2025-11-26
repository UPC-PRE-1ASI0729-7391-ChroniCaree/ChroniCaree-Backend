package com.chronicare.platform.medicalRecords.domain.model.commands;

import com.chronicare.platform.medicalRecords.domain.model.valueobjects.RecordType;
import com.chronicare.platform.medicalRecords.domain.model.valueobjects.ReviewStatus;

/**
 * Command to update an existing medical record
 */
public record UpdateMedicalRecordCommand(
        Long medicalRecordId,
        Long patientId,
        Long doctorId,
        RecordType type,
        String date,
        Double glucose,
        String bloodPressure,
        Integer heartRate,
        Double temperature,
        Double weight,
        Integer fatigue,
        Integer pain,
        Integer dizziness,
        String diagnosis,
        String treatment,
        String notes,
        String patientName,
        ReviewStatus reviewStatus,
        String reviewedAt,
        Long reviewedBy
) {
    public UpdateMedicalRecordCommand {
        if (medicalRecordId == null) {
            throw new IllegalArgumentException("Medical record ID cannot be null");
        }
    }
}

