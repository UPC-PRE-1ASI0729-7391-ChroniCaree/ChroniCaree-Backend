/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patients.domain.commands;
import  com.example.patients.domain.valueobjects.Dni;

/**
 *
 * @author Barturen
 */
public record UpdatePatientCommand(Long patientId,
        String firstName,
        String lastName,
        Dni dni,
        String birthDate,
        String gender,
        String phone,
        String address,
        Double weight,
        Double height) {

    public UpdatePatientCommand          {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("patientId invalid");
        }
        if (firstName == null) {
            throw new IllegalArgumentException("firstName cannot be null");
        }
        if (lastName == null) {
            throw new IllegalArgumentException("lastName cannot be null");
        }
        if (dni == null || dni.value() == null || dni.value().isEmpty()) {
            throw new IllegalArgumentException("DNI cannot be null or empty");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("birthDate cannot be null");
        }
        if (gender == null) {
            throw new IllegalArgumentException("gender cannot be null");
        }
        if (phone == null) {
            throw new IllegalArgumentException("phone cannot be null");
        }
        if (address == null) {
            throw new IllegalArgumentException("address cannot be null");
        }
        if (weight == null) {
            throw new IllegalArgumentException("weight cannot be null");
        }
        if (height == null) {
            throw new IllegalArgumentException("height cannot be null");
        }
    }

}
