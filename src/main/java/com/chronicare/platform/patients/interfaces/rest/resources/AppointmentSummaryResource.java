package com.chronicare.platform.patients.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Appointment Summary Resource
 */
public record AppointmentSummaryResource(
    @JsonProperty("appointmentId") Long appointmentId,
    @JsonProperty("doctorId") Long doctorId,
    @JsonProperty("doctorName") String doctorName,
    @JsonProperty("specialty") String specialty,
    @JsonProperty("appointmentDateTime") LocalDateTime appointmentDateTime,
    @JsonProperty("status") String status,
    @JsonProperty("appointmentType") String appointmentType,
    @JsonProperty("location") String location,
    @JsonProperty("notes") String notes
) {}
