package com.chronicare.platform.medication.interfaces.rest.resources;

import java.util.List;

/**
 * Resource representing medication schedule details
 * This matches the frontend expected structure with schedule.frequency and schedule.times
 */
public record MedicationScheduleResource(
        String frequency,        // "ONCE_DAILY", "TWICE_DAILY", etc.
        List<String> times,      // ["09:00", "21:00"]
        String startDate,        // "2025-12-05" (ISO date format)
        String endDate           // "2026-01-05" (ISO date format, optional)
) {
}
