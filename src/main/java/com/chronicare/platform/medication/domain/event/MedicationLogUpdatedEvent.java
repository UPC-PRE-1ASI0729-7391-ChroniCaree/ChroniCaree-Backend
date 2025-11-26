/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.event;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class MedicationLogUpdatedEvent extends ApplicationEvent {

    private final Long logId;
    private final Long medicationId;
    private final LocalDateTime logTimestamp;
    private final String action;

    public MedicationLogUpdatedEvent(Object source, Long logId, Long medicationId,
            LocalDateTime logTimestamp, String action) {
        super(source);
        this.logId = logId;
        this.medicationId = medicationId;
        this.logTimestamp = logTimestamp;
        this.action = action;
    }

    public Long getLogId() {
        return logId;
    }

    public Long getMedicationId() {
        return medicationId;
    }

    public LocalDateTime getLogTimestamp() {
        return logTimestamp;
    }

    public String getAction() {
        return action;
    }
}
