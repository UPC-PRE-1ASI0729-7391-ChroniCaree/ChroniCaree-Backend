/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.symptoms.domain.events;

import com.example.symptoms.domain.aggregate.Symptom;
import org.springframework.context.ApplicationEvent;

public class SymptomCreatedEvent extends ApplicationEvent {

    private final Symptom symptom;

    public SymptomCreatedEvent(Object source, Symptom symptom) {
        super(source);
        this.symptom = symptom;
    }

    public Symptom getSymptom() {
        return symptom;
    }
}
