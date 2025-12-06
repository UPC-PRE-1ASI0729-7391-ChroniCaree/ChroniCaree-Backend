package com.chronicare.platform.patients.domain.commands;

/**
 * DeletePatientCommand
 *
 * @summary
 * Represents the intent to remove an existing patient from the system.
 * Responsibilities:
 * - Encapsulates the patient identifier to delete
 * - Ensforces minimal validation to guarantee ID integrity
 * Notes:
 * - Patient ID must be a positive, non-null value
 */


public record DeletePatientCommand ( Long patientId) {
        public DeletePatientCommand {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("tenantId invalid");
        }
    }
}
