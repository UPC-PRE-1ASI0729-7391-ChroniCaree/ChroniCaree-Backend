package com.chronicare.platform.patients.domain.commands;
import  com.chronicare.platform.patients.domain.valueobjects.Dni;

 
/**
 * Command to create a new patient
 */
public record CreatePatientCommand(
        String firstName,
        String lastName,
        Dni dni,
        String birthDate,
        String gender,
        String phone,
        String address,
        Double weight,
        Double height
) {
    public CreatePatientCommand {
        if (dni == null || dni.value() == null || dni.value().isEmpty()) {
            throw new IllegalArgumentException("DNI cannot be null or empty");
        }
    }
}
