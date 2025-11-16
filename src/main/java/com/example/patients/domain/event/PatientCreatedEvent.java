/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patients.domain.event;

import org.springframework.context.ApplicationEvent;
import  com.example.patients.domain.valueobjects.Dni;

public class PatientCreatedEvent extends ApplicationEvent {

    private final Long patientId;
    private final Dni dni;

    public PatientCreatedEvent(Object source, Long patientId, Dni dni) {
        super(source);
        this.patientId = patientId;
        this.dni = dni;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Dni getDni() {
        return dni;
    }
}
