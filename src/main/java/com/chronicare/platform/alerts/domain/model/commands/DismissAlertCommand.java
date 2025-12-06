package com.chronicare.platform.alerts.domain.model.commands;

/**
 * Summary: Command to dismiss an alert
 */
public record DismissAlertCommand(
    Long alertId,
    Long dismissedBy,
    String notes
) {
    public DismissAlertCommand {
        if (alertId == null) {
            throw new IllegalArgumentException("alertId is required");
        }
    }
}
