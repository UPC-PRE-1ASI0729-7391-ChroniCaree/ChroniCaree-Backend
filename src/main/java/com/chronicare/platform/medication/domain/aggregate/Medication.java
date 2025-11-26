/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.aggregate;

import com.chronicare.platform.medication.domain.command.CreateMedicationCommand;
import com.chronicare.platform.medication.domain.command.UpdateMedicationCommand;
import com.chronicare.platform.medication.domain.valueobject.MedicationTypeVO;
import com.chronicare.platform.medication.domain.valueobject.MedicationSchedule;
import com.chronicare.platform.medication.domain.valueobject.MedicationStatusVO;
 
 
import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "medications")
@NoArgsConstructor
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- CAMBIO IMPORTANTE ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private MedicationTypeVO type;

    @Column(nullable = false)
    private String dosage;

    @Embedded
    private MedicationSchedule schedule;

    @Column(nullable = false)
    private String prescribedBy;

    @Column(nullable = false)
    private LocalDate prescribedDate;

    @Embedded
    @Column(nullable = false)
    private MedicationStatusVO status;

    private String instructions;

    @ElementCollection
    private List<String> sideEffects;

    @ElementCollection
    private List<String> contraindications;

    private String purpose;

    private LocalDate refillDate;

    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationLog> logs;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ==================== LIFECYCLE HOOKS ====================
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ==================== UPDATE METHODS ====================
    public void updateFrom(CreateMedicationCommand command) {
        this.name = command.name();
        this.type = command.type();
        this.dosage = command.dosage();
        this.schedule = command.schedule();
        this.prescribedBy = command.prescribedBy();
        this.prescribedDate = command.prescribedDate();
        this.status = command.status();
        this.instructions = command.instructions();
        this.sideEffects = command.sideEffects();
        this.contraindications = command.contraindications();
        this.purpose = command.purpose();
        this.refillDate = command.refillDate();
    }

    public void updateFrom(UpdateMedicationCommand command) {
        this.name = command.name();
        this.type = command.type();
        this.dosage = command.dosage();
        this.schedule = command.schedule();
        this.prescribedBy = command.prescribedBy();
        this.prescribedDate = command.prescribedDate();
        this.status = command.status();
        this.instructions = command.instructions();
        this.sideEffects = command.sideEffects();
        this.contraindications = command.contraindications();
        this.purpose = command.purpose();
        this.refillDate = command.refillDate();
    }

    // --- Se agrega un setter explícito para el Patient ---
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
