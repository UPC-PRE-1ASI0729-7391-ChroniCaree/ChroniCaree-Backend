package com.chronicare.platform.alerts.domain.model.queries;

/**
 * Summary: Query to get alert by ID
 */
public record GetAlertByIdQuery(Long alertId) {
    public GetAlertByIdQuery {
        if (alertId == null) {
            throw new IllegalArgumentException("alertId is required");
        }
    }
}
