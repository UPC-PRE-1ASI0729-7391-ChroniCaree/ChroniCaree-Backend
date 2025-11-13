/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.diagnosis.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnoses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
