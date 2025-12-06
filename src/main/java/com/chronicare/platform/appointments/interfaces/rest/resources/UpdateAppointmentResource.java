package com.chronicare.platform.appointments.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Summary: Update Appointment Request Resource
 */
public record UpdateAppointmentResource(
    @JsonProperty("appointmentDate") LocalDate appointmentDate,
    @JsonProperty("appointmentTime") LocalTime appointmentTime,
    @JsonProperty("appointmentType") String appointmentType,
    @JsonProperty("reason") String reason,
    @JsonProperty("location") String location,
    @JsonProperty("notes") String notes,
    @JsonProperty("duration") Integer duration
) {}
