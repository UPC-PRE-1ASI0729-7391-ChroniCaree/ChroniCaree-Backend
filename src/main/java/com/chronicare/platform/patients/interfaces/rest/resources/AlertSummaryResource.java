package com.chronicare.platform.patients.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Alert Summary Resource
 */
public record AlertSummaryResource(
    @JsonProperty("alertId") Long alertId,
    @JsonProperty("alertType") String alertType,
    @JsonProperty("severity") String severity,
    @JsonProperty("message") String message,
    @JsonProperty("triggeredAt") LocalDateTime triggeredAt,
    @JsonProperty("isRead") Boolean isRead,
    @JsonProperty("actionRequired") String actionRequired,
    @JsonProperty("dismissedAt") LocalDateTime dismissedAt
) {}
