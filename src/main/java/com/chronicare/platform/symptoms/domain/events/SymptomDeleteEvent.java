/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.symptoms.domain.events;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author Barturen
 */
public class SymptomDeleteEvent extends ApplicationEvent {
    
        private final Long symptomId;

    public SymptomDeleteEvent(Object source, Long symptomId) {
        super(source );
        this.symptomId = symptomId;
    }

    public Long symptomId() {
        return symptomId;
    }
    
}
