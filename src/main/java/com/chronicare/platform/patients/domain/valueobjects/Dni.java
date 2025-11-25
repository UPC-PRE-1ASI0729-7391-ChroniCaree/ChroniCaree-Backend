/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.patients.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

@Embeddable
public class Dni {

    private String value;

    public Dni() {
    }

    @JsonCreator
    public Dni(@JsonProperty("value") String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("DNI cannot be null or empty");
        }
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
