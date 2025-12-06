package com.chronicare.platform.appointments.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Summary: Cancel Appointment Request Resource
 */
public record CancelAppointmentResource(
    @JsonProperty("cancellationReason") String cancellationReason
) {}
