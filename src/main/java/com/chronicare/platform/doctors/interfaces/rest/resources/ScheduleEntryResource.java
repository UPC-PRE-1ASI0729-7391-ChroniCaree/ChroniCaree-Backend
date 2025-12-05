package com.chronicare.platform.doctors.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

/**
 * Schedule Entry Resource
 */
public record ScheduleEntryResource(
    @JsonProperty("dayOfWeek") String dayOfWeek,
    @JsonProperty("startTime") LocalTime startTime,
    @JsonProperty("endTime") LocalTime endTime,
    @JsonProperty("appointmentDuration") Integer appointmentDuration,
    @JsonProperty("isAvailable") Boolean isAvailable
) {}
