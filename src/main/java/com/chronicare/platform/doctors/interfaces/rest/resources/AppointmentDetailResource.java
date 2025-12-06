package com.chronicare.platform.doctors.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Summary: Appointment Detail Resource
 */
public record AppointmentDetailResource(
    @JsonProperty("appointmentId") Long appointmentId,
    @JsonProperty("patientId") Long patientId,
    @JsonProperty("patientName") String patientName,
    @JsonProperty("patientAge") Integer patientAge,
    @JsonProperty("appointmentDateTime") LocalDateTime appointmentDateTime,
    @JsonProperty("status") String status,
    @JsonProperty("appointmentType") String appointmentType,
    @JsonProperty("reason") String reason,
    @JsonProperty("location") String location,
    @JsonProperty("notes") String notes,
    @JsonProperty("isUrgent") Boolean isUrgent
) {}
