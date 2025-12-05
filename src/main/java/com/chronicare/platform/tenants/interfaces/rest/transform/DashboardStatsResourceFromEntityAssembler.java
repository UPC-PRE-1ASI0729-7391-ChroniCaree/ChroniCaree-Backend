package com.chronicare.platform.tenants.interfaces.rest.transform;

import com.chronicare.platform.tenants.domain.model.aggregates.DashboardStats;
import com.chronicare.platform.tenants.domain.model.valueobjects.QuotaInfo;
import com.chronicare.platform.tenants.domain.model.valueobjects.QuotaUsage;
import com.chronicare.platform.tenants.interfaces.rest.resources.DashboardStatsResource;
import com.chronicare.platform.tenants.interfaces.rest.resources.QuotaInfoResource;
import com.chronicare.platform.tenants.interfaces.rest.resources.QuotaUsageResource;

/**
 * Assembler to transform DashboardStats entity to DashboardStatsResource
 */
public class DashboardStatsResourceFromEntityAssembler {
    
    public static DashboardStatsResource toResourceFromEntity(DashboardStats stats) {
        return new DashboardStatsResource(
            stats.getTenantId(),
            stats.getTenantName(),
            stats.getTotalDoctors(),
            stats.getActiveDoctors(),
            stats.getInactiveDoctors(),
            stats.getTotalPatients(),
            stats.getActivePatients(),
            stats.getNewPatientsThisMonth(),
            stats.getTotalAppointments(),
            stats.getPendingAppointments(),
            stats.getCompletedAppointments(),
            stats.getCancelledAppointments(),
            stats.getTodayAppointments(),
            stats.getCompletedAppointmentsThisMonth(),
            stats.getActiveAlerts(),
            stats.getCriticalAlerts(),
            stats.getHighPriorityAlerts(),
            stats.getSubscriptionStatus(),
            stats.getSubscriptionPlan(),
            stats.getSubscriptionExpiresAt(),
            stats.getTotalRevenue(),
            stats.getMonthlyRevenue(),
            toQuotaUsageResource(stats.getQuotaUsage())
        );
    }
    
    private static QuotaUsageResource toQuotaUsageResource(QuotaUsage quotaUsage) {
        return new QuotaUsageResource(
            toQuotaInfoResource(quotaUsage.doctors()),
            toQuotaInfoResource(quotaUsage.patients())
        );
    }
    
    private static QuotaInfoResource toQuotaInfoResource(QuotaInfo quotaInfo) {
        return new QuotaInfoResource(
            quotaInfo.used(),
            quotaInfo.max(),
            quotaInfo.percentage()
        );
    }
}
