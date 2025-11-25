package com.chronicare.platform.symptoms.domain.aggregate;

import com.chronicare.platform.patients.domain.aggregates.Patient;
import com.chronicare.platform.symptoms.domain.commands.CreateSymptomCommand;
import com.chronicare.platform.symptoms.domain.commands.UpdateSymptomCommand;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "symptoms")
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ahora Symptom pertenece a un Patient
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

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

    public Symptom(CreateSymptomCommand command, Patient patient) {
        this.patient = patient;

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
