/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patients.domain.commands;
import  com.example.patients.domain.valueobjects.Dni;

 
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
