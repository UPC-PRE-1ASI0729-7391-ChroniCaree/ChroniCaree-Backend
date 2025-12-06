package com.chronicare.platform.alerts.domain.model.commands;

/**
 * Summary: Command to acknowledge an alert
 */
public record AcknowledgeAlertCommand(
    Long alertId,
    Long acknowledgedBy,
    String notes
) {
    public AcknowledgeAlertCommand {
        if (alertId == null) {
            throw new IllegalArgumentException("alertId is required");
        }
        if (acknowledgedBy == null) {
            throw new IllegalArgumentException("acknowledgedBy is required");
        }
    }
}
