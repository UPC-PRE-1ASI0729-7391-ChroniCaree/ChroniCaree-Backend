package com.chronicare.platform.alerts.interfaces.rest.resources;

/**
 * Resource for alert statistics
 */
public record AlertStatsResource(
    int activeCount,
    int acknowledgedCount,
    int resolvedCount,
    int escalatedCount,
    int criticalCount,
    int highCount,
    int totalPending
) {}
