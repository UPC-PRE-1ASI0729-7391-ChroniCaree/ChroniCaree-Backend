package com.chronicare.platform.appointments.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Complete Appointment Request Resource
 */
public record CompleteAppointmentResource(
    @JsonProperty("completionNotes") String completionNotes
) {}
