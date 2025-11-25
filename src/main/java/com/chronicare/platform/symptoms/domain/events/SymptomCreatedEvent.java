/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.symptoms.domain.events;

import com.chronicare.platform.symptoms.domain.aggregate.Symptom;
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
