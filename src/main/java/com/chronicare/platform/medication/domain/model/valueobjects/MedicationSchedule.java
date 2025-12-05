package com.chronicare.platform.medication.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

/**
 * Value Object representing medication schedule
 */
@Embeddable
@Getter
public class MedicationSchedule {

    @Enumerated(EnumType.STRING)
    private MedicationFrequency frequency;
    
    private String timeOfDay;

    public MedicationSchedule() {
    }

    public MedicationSchedule(MedicationFrequency frequency, String timeOfDay) {
        this.frequency = frequency;
        this.timeOfDay = timeOfDay;
    }
}
