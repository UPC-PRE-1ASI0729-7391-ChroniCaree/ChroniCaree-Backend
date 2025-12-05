package com.chronicare.platform.diagnosis.domain.model.aggregates;

import com.chronicare.platform.diagnosis.domain.model.commands.CreateDiagnosisCommand;
import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisSeverity;
import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Diagnosis Aggregate Root
 * Represents a medical diagnosis for a patient
 */
@Entity
@Table(name = "diagnoses")
@Getter
@Setter
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long doctorId;

    @Column(nullable = false)
    private String icd10Code;

    @Column(nullable = false)
    private String diagnosisName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiagnosisStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiagnosisSeverity severity;

    @Column(nullable = false)
    private LocalDate diagnosedDate;

    private LocalDate resolvedDate;
    private String notes;
    private String treatment;

    @Column(nullable = false)
    private boolean followUpRequired;

    private LocalDate lastReviewDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * Default constructor required by JPA
     */
    public Diagnosis() {
    }

    /**
     * Constructor for creating a new Diagnosis from a command
     * @param command The CreateDiagnosisCommand containing diagnosis data
     */
    public Diagnosis(CreateDiagnosisCommand command) {
        this.patientId = command.patientId();
        this.doctorId = command.doctorId();
        this.icd10Code = command.icd10Code();
        this.diagnosisName = command.diagnosisName();
        this.status = command.status();
        this.severity = command.severity();
        this.diagnosedDate = command.diagnosedDate();
        this.resolvedDate = command.resolvedDate();
        this.notes = command.notes();
        this.treatment = command.treatment();
        this.followUpRequired = command.followUpRequired();
        this.lastReviewDate = command.lastReviewDate();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Update diagnosis details
     */
    public Diagnosis updateDiagnosis(String icd10Code, String diagnosisName, DiagnosisStatus status,
                                      DiagnosisSeverity severity, LocalDate resolvedDate, String notes,
                                      String treatment, boolean followUpRequired, LocalDate lastReviewDate) {
        this.icd10Code = icd10Code;
        this.diagnosisName = diagnosisName;
        this.status = status;
        this.severity = severity;
        this.resolvedDate = resolvedDate;
        this.notes = notes;
        this.treatment = treatment;
        this.followUpRequired = followUpRequired;
        this.lastReviewDate = lastReviewDate;
        return this;
    }
}
