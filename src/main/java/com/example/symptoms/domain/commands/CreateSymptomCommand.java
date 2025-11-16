/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.symptoms.domain.commands;

 
public record CreateSymptomCommand(
        Long patientId,
        Double glucose,
        String bloodPressure,
        Integer heartRate,
        Double temperature,
        Double oxygenSaturation,
        Integer fatigue,
        Integer pain,
        Integer dizziness,
        String notes
        ) {

}
