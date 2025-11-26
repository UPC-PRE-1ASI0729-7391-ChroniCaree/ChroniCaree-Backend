/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class MedicationTypeVO {

    @Column(name = "type", nullable = false)
    private String value;

    public MedicationTypeVO(Type type) {
        this.value = type.name();
    }

    public enum Type {
        PILL,
        CAPSULE,
        LIQUID,
        INJECTION,
        INHALER,
        CREAM,
        DROPS,
        PATCH
    }
}
