package com.chronicare.platform.patients.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Medication Summary Resource
 */
public record MedicationSummaryResource(
    @JsonProperty("medicationId") Long medicationId,
    @JsonProperty("medicationName") String medicationName,
    @JsonProperty("dosage") String dosage,
    @JsonProperty("frequency") String frequency,
    @JsonProperty("startDate") LocalDateTime startDate,
    @JsonProperty("endDate") LocalDateTime endDate,
    @JsonProperty("prescribedBy") String prescribedBy,
    @JsonProperty("instructions") String instructions,
    @JsonProperty("isActive") Boolean isActive
) {}
