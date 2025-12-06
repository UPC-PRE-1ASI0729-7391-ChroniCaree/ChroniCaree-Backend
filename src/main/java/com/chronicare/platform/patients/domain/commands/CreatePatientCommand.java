package com.chronicare.platform.patients.domain.commands;
import  com.chronicare.platform.patients.domain.valueobjects.Dni;

/**
 * CreatePatientCommand
 *
 * @summary
 * Represents the intent to create a new Patient within a specific tenant.
 * Responsibilities:
 * - Encapsulates all required patient demographic and medical baseline data
 * - Ensures DNI integrity through basic validation
 * - Serves as an immutable input structure for PatientCommandService
 * Notes:
 * - Validates that DNI cannot be null or empty
 * - Birth date is passed as string and should be normalized/validated at the application layer
 */

public record CreatePatientCommand(
        Long userId,
        Long tenantId,         // Hospital/Clinic that manages this patient
        String firstName,
        String lastName,
        String email,
        Dni dni,
        String birthDate,
        String gender,
        String phone,
        String address,
        String photoUrl,
        Double weight,
        Double height
) {
    public CreatePatientCommand {
        if (dni == null || dni.value() == null || dni.value().isEmpty()) {
            throw new IllegalArgumentException("DNI cannot be null or empty");
        }
    }
}
