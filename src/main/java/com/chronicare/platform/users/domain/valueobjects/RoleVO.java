/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.users.domain.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Role como VO (wrapper sobre enum)
 */
@Getter
@Embeddable
@NoArgsConstructor
public class RoleVO implements Serializable {

    @Column(name = "role", length = 30, nullable = false)
    private String value;

    public RoleVO(Role role) {
        this.value = role.name();
    }

    public enum Role {
        PATIENT,
        DOCTOR,
        HOSPITAL_ADMIN
    }
}
