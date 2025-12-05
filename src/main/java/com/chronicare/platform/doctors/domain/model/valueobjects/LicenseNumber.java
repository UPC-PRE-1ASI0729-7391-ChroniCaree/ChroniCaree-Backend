package com.chronicare.platform.doctors.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class LicenseNumber {
    private String value;

    protected LicenseNumber() {}

    public LicenseNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("License number cannot be empty");
        }
        this.value = value;
    }
}
