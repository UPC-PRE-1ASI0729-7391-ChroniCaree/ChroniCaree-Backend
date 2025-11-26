/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.command;

import java.time.LocalDateTime;

/**
 * Command to update an existing MedicationLog
 */
public record UpdateMedicationLogCommand(
        Long id,
        Long medicationId,
        LocalDateTime timestamp,
        String action
        ) {

    public UpdateMedicationLogCommand    {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id cannot be null or negative");
        }
        if (medicationId == null || medicationId <= 0) {
            throw new IllegalArgumentException("medicationId cannot be null or negative");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp cannot be null");
        }
        if (action == null || action.isEmpty()) {
            throw new IllegalArgumentException("action cannot be null or empty");
        }
    }
}
