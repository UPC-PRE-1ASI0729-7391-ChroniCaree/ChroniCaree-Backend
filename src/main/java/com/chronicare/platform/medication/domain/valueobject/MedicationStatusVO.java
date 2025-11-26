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
public class MedicationStatusVO {

    @Column(name = "status", nullable = false)
    private String value;

    public MedicationStatusVO(Status status) {
        this.value = status.name();
    }

    public enum Status {
        ACTIVE,
        TAKEN,
        MISSED,
        SCHEDULED,
        DISCONTINUED
    }
}
