/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.medicalRecords.domain.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un registro médico de un paciente.
 */
@Entity
@Table(name = "medical_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
