package com.chronicare.platform.doctors.domain.model.valueobjects;

import java.time.LocalTime;

/**
 * Summary: Schedule Entry Value Object
 * Represents a doctor's schedule entry
 */
public record ScheduleEntry(
    String dayOfWeek,
    LocalTime startTime,
    LocalTime endTime,
    Integer appointmentDuration,
    Boolean isAvailable
) {
    public ScheduleEntry {
        if (dayOfWeek == null || dayOfWeek.isBlank()) throw new IllegalArgumentException("Day of week cannot be null or blank");
        if (startTime == null) throw new IllegalArgumentException("Start time cannot be null");
        if (endTime == null) throw new IllegalArgumentException("End time cannot be null");
        if (startTime.isAfter(endTime)) throw new IllegalArgumentException("Start time must be before end time");
        if (isAvailable == null) throw new IllegalArgumentException("Available status cannot be null");
    }
    
    public boolean isWorkingDay() {
        return isAvailable && !startTime.equals(endTime);
    }
    
    public int getTotalWorkingMinutes() {
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endMinutes = endTime.getHour() * 60 + endTime.getMinute();
        return endMinutes - startMinutes;
    }
    
    public int getMaxAppointmentsPerDay() {
        if (!isWorkingDay() || appointmentDuration == null || appointmentDuration <= 0) {
            return 0;
        }
        return getTotalWorkingMinutes() / appointmentDuration;
    }
}
