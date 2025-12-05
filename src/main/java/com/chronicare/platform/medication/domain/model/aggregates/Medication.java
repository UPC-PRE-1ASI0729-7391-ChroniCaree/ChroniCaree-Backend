package com.chronicare.platform.medication.domain.model.aggregates;

import com.chronicare.platform.medication.domain.model.commands.CreateMedicationCommand;
import com.chronicare.platform.medication.domain.model.entities.MedicationLog;
import com.chronicare.platform.medication.domain.model.valueobjects.MedicationFrequency;
import com.chronicare.platform.medication.domain.model.valueobjects.MedicationSchedule;
import com.chronicare.platform.medication.domain.model.valueobjects.MedicationStatus;
import com.chronicare.platform.medication.domain.model.valueobjects.MedicationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Medication Aggregate Root
 * Represents a medication prescription for a patient
 */
@Entity
@Table(name = "medications")
@Getter
@Setter
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicationType type;

    @Column(nullable = false)
    private String dosage;

    @Embedded
    private MedicationSchedule schedule;

    @Column(nullable = false)
    private String prescribedBy;

    @Column(nullable = false)
    private LocalDate prescribedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicationStatus status;

    private String instructions;

    @ElementCollection
    private List<String> sideEffects;

    @ElementCollection
    private List<String> contraindications;

    private String purpose;
    private LocalDate refillDate;

    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationLog> logs = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Default constructor required by JPA
     */
    public Medication() {
    }

    /**
     * Constructor for creating a new Medication from a command
     * @param command The CreateMedicationCommand containing medication data
     */
    public Medication(CreateMedicationCommand command) {
        this.patientId = command.patientId();
        this.name = command.name();
        this.type = command.type();
        this.dosage = command.dosage();
        this.schedule = new MedicationSchedule(command.frequency(), command.timeOfDay());
        this.prescribedBy = command.prescribedBy();
        this.prescribedDate = command.prescribedDate();
        this.status = command.status();
        this.instructions = command.instructions();
        this.sideEffects = command.sideEffects() != null ? new ArrayList<>(command.sideEffects()) : new ArrayList<>();
        this.contraindications = command.contraindications() != null ? new ArrayList<>(command.contraindications()) : new ArrayList<>();
        this.purpose = command.purpose();
        this.refillDate = command.refillDate();
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
     * Update medication details
     */
    public Medication updateMedication(String name, MedicationType type, String dosage,
                                        MedicationFrequency frequency, String timeOfDay,
                                        MedicationStatus status, String instructions,
                                        List<String> sideEffects, List<String> contraindications,
                                        String purpose, LocalDate refillDate) {
        this.name = name;
        this.type = type;
        this.dosage = dosage;
        this.schedule = new MedicationSchedule(frequency, timeOfDay);
        this.status = status;
        this.instructions = instructions;
        this.sideEffects = sideEffects != null ? new ArrayList<>(sideEffects) : new ArrayList<>();
        this.contraindications = contraindications != null ? new ArrayList<>(contraindications) : new ArrayList<>();
        this.purpose = purpose;
        this.refillDate = refillDate;
        return this;
    }

    /**
     * Add a log entry to the medication
     */
    public void addLog(MedicationLog log) {
        logs.add(log);
        log.setMedication(this);
    }
}
