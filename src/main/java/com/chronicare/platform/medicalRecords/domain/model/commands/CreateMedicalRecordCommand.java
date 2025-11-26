package com.chronicare.platform.medicalRecords.domain.model.commands;

import com.chronicare.platform.medicalRecords.domain.model.valueobjects.RecordType;
import com.chronicare.platform.medicalRecords.domain.model.valueobjects.ReviewStatus;

/**
 * Command to create a new medical record
 */
public record CreateMedicalRecordCommand(
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
        ReviewStatus reviewStatus
) {
    public CreateMedicalRecordCommand {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Record type cannot be null");
        }
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Date cannot be null or empty");
        }
    }
}

