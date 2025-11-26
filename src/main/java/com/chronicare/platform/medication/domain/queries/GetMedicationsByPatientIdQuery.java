/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.queries;

/**
 *
 * @author Barturen
 */
public record GetMedicationsByPatientIdQuery(Long patientId) {

    public GetMedicationsByPatientIdQuery {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("patientId must be a positive number");
        }
    }
}
