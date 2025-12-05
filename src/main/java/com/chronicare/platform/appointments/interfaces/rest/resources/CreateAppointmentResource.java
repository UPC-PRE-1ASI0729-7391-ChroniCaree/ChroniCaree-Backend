package com.chronicare.platform.appointments.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Create Appointment Request Resource
 */
public record CreateAppointmentResource(
    @JsonProperty("patientId") Long patientId,
    @JsonProperty("doctorId") Long doctorId,
    @JsonProperty("appointmentDate") LocalDate appointmentDate,
    @JsonProperty("appointmentTime") LocalTime appointmentTime,
    @JsonProperty("appointmentType") String appointmentType,
    @JsonProperty("reason") String reason,
    @JsonProperty("location") String location,
    @JsonProperty("notes") String notes,
    @JsonProperty("duration") Integer duration
) {}
