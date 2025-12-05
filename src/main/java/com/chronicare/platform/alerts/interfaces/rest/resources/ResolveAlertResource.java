package com.chronicare.platform.alerts.interfaces.rest.resources;

/**
 * Resource for resolving an alert
 */
public record ResolveAlertResource(
    String notes,
    String resolutionAction
) {}
