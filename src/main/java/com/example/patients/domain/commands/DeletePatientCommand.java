/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patients.domain.commands;
 
public record DeletePatientCommand ( Long patientId) {
        public DeletePatientCommand {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("tenantId invalid");
        }
    }
}
