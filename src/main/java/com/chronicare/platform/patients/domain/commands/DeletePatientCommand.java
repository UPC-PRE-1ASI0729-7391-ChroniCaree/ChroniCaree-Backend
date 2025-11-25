package com.chronicare.platform.patients.domain.commands;
 
public record DeletePatientCommand ( Long patientId) {
        public DeletePatientCommand {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("tenantId invalid");
        }
    }
}
