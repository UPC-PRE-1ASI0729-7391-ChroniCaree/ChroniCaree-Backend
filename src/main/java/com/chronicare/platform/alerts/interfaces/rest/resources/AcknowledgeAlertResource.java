package com.chronicare.platform.alerts.interfaces.rest.resources;

/**
 * Resource for acknowledging an alert
 */
public record AcknowledgeAlertResource(
    String notes
) {}
