/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.patientHealthSummary.domain.model;

import jakarta.persistence.Embeddable;

/**
 *
 * @author Barturen
 */
@Embeddable
public class LastVitalSigns {

    private Double glucose;
    private String bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Double weight;
    private Integer fatigue;
    private Integer pain;
    private Integer dizziness;

    public LastVitalSigns() {
    }

    public LastVitalSigns(Double glucose, String bloodPressure, Integer heartRate, Double temperature, Double weight, Integer fatigue, Integer pain, Integer dizziness) {
        this.glucose = glucose;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.weight = weight;
        this.fatigue = fatigue;
        this.pain = pain;
        this.dizziness = dizziness;
    }

    public Double getGlucose() {
        return glucose;
    }

    public void setGlucose(Double glucose) {
        this.glucose = glucose;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getFatigue() {
        return fatigue;
    }

    public void setFatigue(Integer fatigue) {
        this.fatigue = fatigue;
    }

    public Integer getPain() {
        return pain;
    }

    public void setPain(Integer pain) {
        this.pain = pain;
    }

    public Integer getDizziness() {
        return dizziness;
    }

    public void setDizziness(Integer dizziness) {
        this.dizziness = dizziness;
    }
}
