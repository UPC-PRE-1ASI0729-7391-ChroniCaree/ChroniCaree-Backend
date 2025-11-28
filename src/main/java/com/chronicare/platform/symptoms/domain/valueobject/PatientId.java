/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.symptoms.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

/**
 * Value Object for PatientId
 */
@Embeddable
public class PatientId implements Serializable {

    private Long value;

    public PatientId() {
    }

    public PatientId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("PatientId cannot be null or less than 1");
        }
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
