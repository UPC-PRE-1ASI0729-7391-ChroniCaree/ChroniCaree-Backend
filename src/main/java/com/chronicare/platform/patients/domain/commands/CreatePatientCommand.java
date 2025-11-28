package com.chronicare.platform.patients.domain.commands;
import  com.chronicare.platform.patients.domain.valueobjects.Dni;

 
/**
 * Command to create a new patient
 */
public record CreatePatientCommand(
        Long userId,
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
