package com.chronicare.platform.medicalRecords.domain.model.aggregates;

import com.chronicare.platform.medicalRecords.domain.model.commands.CreateMedicalRecordCommand;
import com.chronicare.platform.medicalRecords.domain.model.commands.UpdateMedicalRecordCommand;
import com.chronicare.platform.medicalRecords.domain.model.valueobjects.RecordType;
import com.chronicare.platform.medicalRecords.domain.model.valueobjects.ReviewStatus;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Medical Record Aggregate Root
 * Represents a medical record of a patient
 */
@Entity
@Table(name = "medical_records")
@Getter
@Setter
public class MedicalRecord extends AuditableAbstractAggregateRoot<MedicalRecord> {

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long doctorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType type;

    @Column(nullable = false)
    private String date;

    private Double glucose;
    private String bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Double weight;
    private Integer fatigue;
    private Integer pain;
    private Integer dizziness;

    private String diagnosis;
    private String treatment;
    private String notes;

    private String patientName;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    private String reviewedAt;
    private Long reviewedBy;

    protected MedicalRecord() {
        // Required by JPA
    }

    public MedicalRecord(CreateMedicalRecordCommand command) {
        this.patientId = command.patientId();
        this.doctorId = command.doctorId();
        this.type = command.type();
        this.date = command.date();
        this.glucose = command.glucose();
        this.bloodPressure = command.bloodPressure();
        this.heartRate = command.heartRate();
        this.temperature = command.temperature();
        this.weight = command.weight();
        this.fatigue = command.fatigue();
        this.pain = command.pain();
        this.dizziness = command.dizziness();
        this.diagnosis = command.diagnosis();
        this.treatment = command.treatment();
        this.notes = command.notes();
        this.patientName = command.patientName();
        this.reviewStatus = command.reviewStatus() != null ? command.reviewStatus() : ReviewStatus.PENDING_REVIEW;
    }

    public void update(UpdateMedicalRecordCommand command) {
        if (command.patientId() != null) {
            this.patientId = command.patientId();
        }
        if (command.doctorId() != null) {
            this.doctorId = command.doctorId();
        }
        if (command.type() != null) {
            this.type = command.type();
        }
        if (command.date() != null) {
            this.date = command.date();
        }
        if (command.glucose() != null) {
            this.glucose = command.glucose();
        }
        if (command.bloodPressure() != null) {
            this.bloodPressure = command.bloodPressure();
        }
        if (command.heartRate() != null) {
            this.heartRate = command.heartRate();
        }
        if (command.temperature() != null) {
            this.temperature = command.temperature();
        }
        if (command.weight() != null) {
            this.weight = command.weight();
        }
        if (command.fatigue() != null) {
            this.fatigue = command.fatigue();
        }
        if (command.pain() != null) {
            this.pain = command.pain();
        }
        if (command.dizziness() != null) {
            this.dizziness = command.dizziness();
        }
        if (command.diagnosis() != null) {
            this.diagnosis = command.diagnosis();
        }
        if (command.treatment() != null) {
            this.treatment = command.treatment();
        }
        if (command.notes() != null) {
            this.notes = command.notes();
        }
        if (command.patientName() != null) {
            this.patientName = command.patientName();
        }
        if (command.reviewStatus() != null) {
            this.reviewStatus = command.reviewStatus();
        }
        if (command.reviewedAt() != null) {
            this.reviewedAt = command.reviewedAt();
        }
        if (command.reviewedBy() != null) {
            this.reviewedBy = command.reviewedBy();
        }
    }
}

