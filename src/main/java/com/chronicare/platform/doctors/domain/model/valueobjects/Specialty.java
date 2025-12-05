package com.chronicare.platform.doctors.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Specialty {
    private String value;

    protected Specialty() {}

    public Specialty(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Specialty cannot be empty");
        }
        this.value = value;
    }
}
