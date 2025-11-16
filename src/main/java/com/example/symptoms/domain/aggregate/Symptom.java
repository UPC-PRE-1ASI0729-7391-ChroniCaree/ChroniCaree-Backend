/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.symptoms.domain.aggregate;

import com.example.symptoms.domain.commands.CreateSymptomCommand;
import com.example.symptoms.domain.commands.UpdateSymptomCommand;
import com.example.symptoms.domain.valueobject.PatientId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Symptom aggregate root
 *
 * @summary Represents the symptoms of a patient.
 */
@Getter
@Entity
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PatientId patientId;
    private Double glucose;
    private String bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Double oxygenSaturation;
    private Integer fatigue;
    private Integer pain;
    private Integer dizziness;
    private String notes;
    private LocalDateTime timestamp;
    private Boolean isEdited;
    private LocalDateTime editedAt;

    public Symptom() {
        this.timestamp = LocalDateTime.now();
        this.isEdited = false;
    }

    public Symptom(CreateSymptomCommand command) {
        this.patientId = command.patientId();
        this.glucose = command.glucose();
        this.bloodPressure = command.bloodPressure();
        this.heartRate = command.heartRate();
        this.temperature = command.temperature();
        this.oxygenSaturation = command.oxygenSaturation();
        this.fatigue = command.fatigue();
        this.pain = command.pain();
        this.dizziness = command.dizziness();
        this.notes = command.notes();
        this.timestamp = LocalDateTime.now();
        this.isEdited = false;
    }

    public void updateSymptoms(UpdateSymptomCommand command) {
        this.glucose = command.glucose();
        this.bloodPressure = command.bloodPressure();
        this.heartRate = command.heartRate();
        this.temperature = command.temperature();
        this.oxygenSaturation = command.oxygenSaturation();
        this.fatigue = command.fatigue();
        this.pain = command.pain();
        this.dizziness = command.dizziness();
        this.notes = command.notes();
        this.isEdited = true;
        this.editedAt = LocalDateTime.now();
    }
}
