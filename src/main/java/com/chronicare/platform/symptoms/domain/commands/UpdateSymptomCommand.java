package com.chronicare.platform.symptoms.domain.commands;

import com.chronicare.platform.symptoms.domain.valueobject.PatientId;

/**
 *
 * @author Barturen
 */
public record UpdateSymptomCommand(
        Long symptomId,
        Double glucose,
        String bloodPressure,
        Integer heartRate,
        Double temperature,
        Double oxygenSaturation,
        Integer fatigue,
        Integer pain,
        Integer dizziness,
        String notes
) {}
