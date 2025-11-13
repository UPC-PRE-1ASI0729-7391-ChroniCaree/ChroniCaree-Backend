/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patientHealthSummary.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Barturen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LastVitalSigns {

    private Double glucose;
    private String bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Double weight;
    private Integer fatigue;
    private Integer pain;
    private Integer dizziness;
}
