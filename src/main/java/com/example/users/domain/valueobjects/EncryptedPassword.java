/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.users.domain.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * ValueObject para password ya hasheada. Nota: requiere dependencia
 * spring-security-crypto.
 */
@Getter
@Embeddable
@NoArgsConstructor
@ToString
public class EncryptedPassword implements Serializable {

    @Column(name = "password", nullable = false)
    private String value;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public EncryptedPassword(String rawOrEncoded, boolean alreadyEncoded) {
        if (rawOrEncoded == null || rawOrEncoded.isBlank()) {
            throw new IllegalArgumentException("password cannot be blank");
        }
        this.value = alreadyEncoded ? rawOrEncoded : encoder.encode(rawOrEncoded);
    }

    public boolean matches(String rawPassword) {
        return encoder.matches(rawPassword, this.value);
    }

    public String getEncoded() {
        return this.value;
    }
}
