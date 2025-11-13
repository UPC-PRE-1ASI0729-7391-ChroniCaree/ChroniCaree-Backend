/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.symptoms.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "symptoms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    private Double glucose;

    private String bloodPressure;

    private Integer heartRate;

    private Double temperature;

    private Double oxygenSaturation;

    private Integer fatigue;

    private Integer pain;

    private Integer dizziness;

    private String notes;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private Boolean isEdited;

    private LocalDateTime editedAt;
}
