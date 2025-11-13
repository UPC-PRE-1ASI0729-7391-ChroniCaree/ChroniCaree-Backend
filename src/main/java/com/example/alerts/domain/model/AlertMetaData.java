/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.alerts.domain.model;

import jakarta.persistence.*;
import lombok.*;
 
@Entity
@Table(name = "alert_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vitalSign;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private Double threshold;

    @Column(nullable = false)
    private String measurement;

    @Column(nullable = true)
    private String medicationId;

    @Column(nullable = true)
    private String symptomId;

    @Column(nullable = true)
    private String appointmentId;

    @Column(columnDefinition = "json")
    private String additionalData;
}
