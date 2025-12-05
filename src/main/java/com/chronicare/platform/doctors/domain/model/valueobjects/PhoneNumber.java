package com.chronicare.platform.doctors.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class PhoneNumber {
    private String value;

    protected PhoneNumber() {}

    public PhoneNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        this.value = value;
    }
}
