package com.chronicare.platform.alerts.domain.model.commands;

/**
 * Command to escalate an alert
 */
public record EscalateAlertCommand(
    Long alertId,
    Long escalatedTo,
    Integer escalationLevel,
    String notes
) {
    public EscalateAlertCommand {
        if (alertId == null) {
            throw new IllegalArgumentException("alertId is required");
        }
        if (escalatedTo == null) {
            throw new IllegalArgumentException("escalatedTo is required");
        }
    }
}
