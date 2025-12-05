package com.chronicare.platform.alerts.interfaces.rest.resources;

/**
 * Resource for escalating an alert
 */
public record EscalateAlertResource(
    Long escalateTo,
    String reason
) {}
