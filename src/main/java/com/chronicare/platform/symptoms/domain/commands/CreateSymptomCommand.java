package com.chronicare.platform.symptoms.domain.commands;

 
public record CreateSymptomCommand(
        Long patientId,
        Double glucose,
        String bloodPressure,
        Integer heartRate,
        Double temperature,
        Double oxygenSaturation,
        Integer fatigue,
        Integer pain,
        Integer dizziness,
        String notes
        ) {

}
