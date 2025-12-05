package com.chronicare.platform.tenants.domain.model.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * QuotaInfo Value Object
 * Represents quota usage information for a specific resource type
 */
public record QuotaInfo(
    Integer used,
    Integer max,
    BigDecimal percentage
) {
    /**
     * Creates a QuotaInfo with calculated percentage
     * @param used Current usage count
     * @param max Maximum allowed count
     * @return QuotaInfo with calculated percentage
     */
    public static QuotaInfo of(Integer used, Integer max) {
        if (max == null || max == 0) {
            return new QuotaInfo(used, max, BigDecimal.ZERO);
        }
        
        BigDecimal percentage = BigDecimal.valueOf(used)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(max), 2, RoundingMode.HALF_UP);
            
        return new QuotaInfo(used, max, percentage);
    }
    
    /**
     * Checks if quota is exceeded
     * @return true if used >= max
     */
    public boolean isExceeded() {
        return max != null && used >= max;
    }
    
    /**
     * Checks if quota is near limit (>= 90%)
     * @return true if usage is >= 90%
     */
    public boolean isNearLimit() {
        return percentage.compareTo(BigDecimal.valueOf(90)) >= 0;
    }
}
