package com.chronicare.platform.appointments.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Summary: Appointment Response Resource
 */
public record AppointmentResource(
    @JsonProperty("id") Long id,
    @JsonProperty("patientId") Long patientId,
    @JsonProperty("doctorId") Long doctorId,
    @JsonProperty("date") String date,
    @JsonProperty("time") String time,
    @JsonProperty("type") String type,
    @JsonProperty("status") String status,
    @JsonProperty("notes") String notes,
    @JsonProperty("patientName") String patientName,
    @JsonProperty("patientPhone") String patientPhone,
    @JsonProperty("patientEmail") String patientEmail,
    @JsonProperty("duration") Integer duration
) {}
