/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.event;

import com.chronicare.platform.medication.domain.valueobject.MedicationSchedule;
import com.chronicare.platform.medication.domain.valueobject.MedicationStatusVO;
import com.chronicare.platform.medication.domain.valueobject.MedicationTypeVO;
import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import java.time.LocalDate;
import org.springframework.context.ApplicationEvent;

public class MedicationCreatedEvent extends ApplicationEvent {

    private final Long medicationId;
    private final Patient patient;
    private final String name;
    private final MedicationTypeVO type;
    private final String dosage;
    private final MedicationSchedule schedule;
    private final String prescribedBy;
    private final LocalDate prescribedDate;
    private final MedicationStatusVO status;

    public MedicationCreatedEvent(
            Object source,
            Long medicationId,
            Patient patient,
            String name,
            MedicationTypeVO type,
            String dosage,
            MedicationSchedule schedule,
            String prescribedBy,
            LocalDate prescribedDate,
            MedicationStatusVO status
    ) {
        super(source);
        this.medicationId = medicationId;
        this.patient = patient;
        this.name = name;
        this.type = type;
        this.dosage = dosage;
        this.schedule = schedule;
        this.prescribedBy = prescribedBy;
        this.prescribedDate = prescribedDate;
        this.status = status;
    }

    public Long getMedicationId() {
        return medicationId;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getName() {
        return name;
    }

    public MedicationTypeVO getType() {
        return type;
    }

    public String getDosage() {
        return dosage;
    }

    public MedicationSchedule getSchedule() {
        return schedule;
    }

    public String getPrescribedBy() {
        return prescribedBy;
    }

    public LocalDate getPrescribedDate() {
        return prescribedDate;
    }

    public MedicationStatusVO getStatus() {
        return status;
    }
}
