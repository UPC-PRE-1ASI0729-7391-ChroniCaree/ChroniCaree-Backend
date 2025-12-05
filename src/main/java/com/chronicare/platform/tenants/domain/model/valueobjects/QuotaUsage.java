package com.chronicare.platform.tenants.domain.model.valueobjects;

/**
 * QuotaUsage Value Object
 * Contains quota information for doctors and patients
 */
public record QuotaUsage(
    QuotaInfo doctors,
    QuotaInfo patients
) {
    /**
     * Creates a QuotaUsage with calculated percentages
     * @param usedDoctors Current doctor count
     * @param maxDoctors Maximum allowed doctors
     * @param usedPatients Current patient count
     * @param maxPatients Maximum allowed patients (nullable)
     * @return QuotaUsage with calculated info
     */
    public static QuotaUsage of(Integer usedDoctors, Integer maxDoctors, 
                                Integer usedPatients, Integer maxPatients) {
        return new QuotaUsage(
            QuotaInfo.of(usedDoctors, maxDoctors),
            QuotaInfo.of(usedPatients, maxPatients)
        );
    }
    
    /**
     * Checks if any quota is exceeded
     * @return true if doctors or patients quota is exceeded
     */
    public boolean hasExceededQuota() {
        return doctors.isExceeded() || patients.isExceeded();
    }
}
