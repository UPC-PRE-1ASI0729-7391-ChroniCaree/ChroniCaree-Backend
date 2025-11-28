package com.chronicare.platform.medicalRecords.domain.model.commands;

/**
 * Command to delete a medical record
 */
public record DeleteMedicalRecordCommand(Long medicalRecordId) {
    public DeleteMedicalRecordCommand {
        if (medicalRecordId == null) {
            throw new IllegalArgumentException("Medical record ID cannot be null");
        }
    }
}

