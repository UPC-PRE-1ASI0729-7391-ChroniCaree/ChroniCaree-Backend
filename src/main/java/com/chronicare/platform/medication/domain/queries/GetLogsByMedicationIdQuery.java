/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.queries;

/**
 *
 * @author Barturen
 */
public record GetLogsByMedicationIdQuery(Long medicationId) {

    public GetLogsByMedicationIdQuery {
        if (medicationId == null || medicationId <= 0) {
            throw new IllegalArgumentException("medicationId cannot be null or negative");
        }
    }
}
