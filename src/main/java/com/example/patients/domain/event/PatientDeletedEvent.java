/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patients.domain.event;

import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when a patient is deleted
 */
public class PatientDeletedEvent extends ApplicationEvent {

    private final Long patientId;

    public PatientDeletedEvent(Object source, Long patientId) {
        super(source);
        this.patientId = patientId;
    }

    public Long getPatientId() {
        return patientId;
    }
}
