/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.queries;

import com.chronicare.platform.medication.domain.valueobject.MedicationStatusVO;

/**
 *
 * @author Barturen
 */
public record GetMedicationsByStatusQuery(MedicationStatusVO status) {

    public GetMedicationsByStatusQuery {
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
    }
}
