/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.command;

import com.chronicare.platform.medication.domain.valueobject.MedicationTypeVO;
import com.chronicare.platform.medication.domain.valueobject.MedicationSchedule;
import com.chronicare.platform.medication.domain.valueobject.MedicationStatusVO;
import java.time.LocalDate;
import java.util.List;

/**
 * Command to update an existing Medication aggregate
 */
public record UpdateMedicationCommand(
        Long id,
        Long patientId, // <-- CAMBIADO de String a Long
        String name,
        MedicationTypeVO type,
        String dosage,
        MedicationSchedule schedule,
        String prescribedBy,
        LocalDate prescribedDate,
        MedicationStatusVO status,
        String instructions,
        List<String> sideEffects,
        List<String> contraindications,
        String purpose,
        LocalDate refillDate
        ) {

    public UpdateMedicationCommand              {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be a valid ID (> 0)");
        }
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("patientId must be a valid ID (> 0)");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (dosage == null || dosage.isEmpty()) {
            throw new IllegalArgumentException("dosage cannot be null or empty");
        }
        if (schedule == null) {
            throw new IllegalArgumentException("schedule cannot be null");
        }
        if (prescribedBy == null || prescribedBy.isEmpty()) {
            throw new IllegalArgumentException("prescribedBy cannot be null or empty");
        }
        if (prescribedDate == null) {
            throw new IllegalArgumentException("prescribedDate cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
    }
}
