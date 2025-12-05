package com.chronicare.platform.patients.domain.model.valueobjects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Vital Signs Summary Value Object
 * Represents latest vital signs for patient dashboard
 */
public record VitalSignsSummary(
    Long recordId,
    BigDecimal bloodPressureSystolic,
    BigDecimal bloodPressureDiastolic,
    BigDecimal heartRate,
    BigDecimal temperature,
    BigDecimal weight,
    BigDecimal height,
    BigDecimal bmi,
    LocalDateTime measuredAt,
    String measuredBy,
    String notes
) {
    public VitalSignsSummary {
        if (recordId == null) throw new IllegalArgumentException("Record ID cannot be null");
        if (measuredAt == null) throw new IllegalArgumentException("Measured at cannot be null");
    }
    
    public boolean hasBloodPressure() {
        return bloodPressureSystolic != null && bloodPressureDiastolic != null;
    }
    
    public boolean hasHeartRate() {
        return heartRate != null;
    }
    
    public boolean hasTemperature() {
        return temperature != null;
    }
    
    public boolean hasWeight() {
        return weight != null;
    }
    
    public boolean hasBMI() {
        return bmi != null;
    }
    
    public String getBloodPressureFormatted() {
        if (!hasBloodPressure()) return "N/A";
        return String.format("%.0f/%.0f", bloodPressureSystolic, bloodPressureDiastolic);
    }
    
    public boolean isRecentReading() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return measuredAt.isAfter(oneDayAgo);
    }
}
