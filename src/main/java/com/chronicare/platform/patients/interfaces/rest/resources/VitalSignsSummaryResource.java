package com.chronicare.platform.patients.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Vital Signs Summary Resource
 */
public record VitalSignsSummaryResource(
    @JsonProperty("recordId") Long recordId,
    @JsonProperty("bloodPressureSystolic") BigDecimal bloodPressureSystolic,
    @JsonProperty("bloodPressureDiastolic") BigDecimal bloodPressureDiastolic,
    @JsonProperty("heartRate") BigDecimal heartRate,
    @JsonProperty("temperature") BigDecimal temperature,
    @JsonProperty("weight") BigDecimal weight,
    @JsonProperty("height") BigDecimal height,
    @JsonProperty("bmi") BigDecimal bmi,
    @JsonProperty("measuredAt") LocalDateTime measuredAt,
    @JsonProperty("measuredBy") String measuredBy,
    @JsonProperty("notes") String notes
) {}
