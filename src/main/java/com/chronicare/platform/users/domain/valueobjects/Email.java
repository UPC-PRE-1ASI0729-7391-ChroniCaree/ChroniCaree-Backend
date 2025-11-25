/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.users.domain.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ValueObject para Email
 */
@Getter
@Embeddable
@NoArgsConstructor
@ToString
public class Email implements Serializable {

    @NotBlank
    @jakarta.validation.constraints.Email
    @Column(name = "email", length = 150, nullable = false)
    private String value;

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("email cannot be blank");
        }
        // simple validation - you can add stricter checks
        this.value = value.toLowerCase();
    }
}
