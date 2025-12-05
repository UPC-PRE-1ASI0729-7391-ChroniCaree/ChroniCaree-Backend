package com.chronicare.platform.alerts.domain.model.commands;

/**
 * Command to resolve an alert
 */
public record ResolveAlertCommand(
    Long alertId,
    Long resolvedBy,
    String resolutionNotes,
    String resolutionAction
) {
    public ResolveAlertCommand {
        if (alertId == null) {
            throw new IllegalArgumentException("alertId is required");
        }
        if (resolvedBy == null) {
            throw new IllegalArgumentException("resolvedBy is required");
        }
    }
}
