package com.chronicare.platform.doctors.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Patient Summary Resource
 */
public record PatientSummaryResource(
    @JsonProperty("patientId") Long patientId,
    @JsonProperty("patientName") String patientName,
    @JsonProperty("age") Integer age,
    @JsonProperty("gender") String gender,
    @JsonProperty("bloodType") String bloodType,
    @JsonProperty("contactPhone") String contactPhone,
    @JsonProperty("lastVisit") LocalDateTime lastVisit,
    @JsonProperty("totalVisits") Integer totalVisits,
    @JsonProperty("hasActiveAlerts") Boolean hasActiveAlerts,
    @JsonProperty("criticalAlerts") Integer criticalAlerts,
    @JsonProperty("currentCondition") String currentCondition
) {}
