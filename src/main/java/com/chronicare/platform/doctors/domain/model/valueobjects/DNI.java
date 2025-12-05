package com.chronicare.platform.doctors.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class DNI {
    private String value;

    protected DNI() {}

    public DNI(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DNI cannot be empty");
        }
        if (value.length() < 8 || value.length() > 20) {
            throw new IllegalArgumentException("DNI must be between 8 and 20 characters");
        }
        this.value = value;
    }
}
