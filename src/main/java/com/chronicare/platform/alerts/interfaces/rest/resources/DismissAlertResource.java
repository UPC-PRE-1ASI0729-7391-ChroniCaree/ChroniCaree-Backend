package com.chronicare.platform.alerts.interfaces.rest.resources;

/**
 * Resource for dismissing an alert
 */
public record DismissAlertResource(
    String reason
) {}
