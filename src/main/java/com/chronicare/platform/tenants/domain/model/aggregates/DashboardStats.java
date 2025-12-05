package com.chronicare.platform.tenants.domain.model.aggregates;

import com.chronicare.platform.tenants.domain.model.valueobjects.QuotaUsage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DashboardStats Aggregate Root
 * Represents comprehensive dashboard statistics for a tenant
 */
public class DashboardStats {
    private final Long tenantId;
    private final String tenantName;
    private final Integer totalDoctors;
    private final Integer activeDoctors;
    private final Integer inactiveDoctors;
    private final Integer totalPatients;
    private final Integer activePatients;
    private final Integer newPatientsThisMonth;
    private final Integer totalAppointments;
    private final Integer pendingAppointments;
    private final Integer completedAppointments;
    private final Integer cancelledAppointments;
    private final Integer todayAppointments;
    private final Integer completedAppointmentsThisMonth;
    private final Integer activeAlerts;
    private final Integer criticalAlerts;
    private final Integer highPriorityAlerts;
    private final String subscriptionStatus;
    private final String subscriptionPlan;
    private final LocalDateTime subscriptionExpiresAt;
    private final BigDecimal totalRevenue;
    private final BigDecimal monthlyRevenue;
    private final QuotaUsage quotaUsage;

    public DashboardStats(Long tenantId, String tenantName, Integer totalDoctors, 
                          Integer activeDoctors, Integer inactiveDoctors, Integer totalPatients, 
                          Integer activePatients, Integer newPatientsThisMonth, Integer totalAppointments, 
                          Integer pendingAppointments, Integer completedAppointments, 
                          Integer cancelledAppointments, Integer todayAppointments, 
                          Integer completedAppointmentsThisMonth, Integer activeAlerts, 
                          Integer criticalAlerts, Integer highPriorityAlerts, String subscriptionStatus, 
                          String subscriptionPlan, LocalDateTime subscriptionExpiresAt, 
                          BigDecimal totalRevenue, BigDecimal monthlyRevenue, QuotaUsage quotaUsage) {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.totalDoctors = totalDoctors;
        this.activeDoctors = activeDoctors;
        this.inactiveDoctors = inactiveDoctors;
        this.totalPatients = totalPatients;
        this.activePatients = activePatients;
        this.newPatientsThisMonth = newPatientsThisMonth;
        this.totalAppointments = totalAppointments;
        this.pendingAppointments = pendingAppointments;
        this.completedAppointments = completedAppointments;
        this.cancelledAppointments = cancelledAppointments;
        this.todayAppointments = todayAppointments;
        this.completedAppointmentsThisMonth = completedAppointmentsThisMonth;
        this.activeAlerts = activeAlerts;
        this.criticalAlerts = criticalAlerts;
        this.highPriorityAlerts = highPriorityAlerts;
        this.subscriptionStatus = subscriptionStatus;
        this.subscriptionPlan = subscriptionPlan;
        this.subscriptionExpiresAt = subscriptionExpiresAt;
        this.totalRevenue = totalRevenue;
        this.monthlyRevenue = monthlyRevenue;
        this.quotaUsage = quotaUsage;
    }

    // Getters
    public Long getTenantId() { return tenantId; }
    public String getTenantName() { return tenantName; }
    public Integer getTotalDoctors() { return totalDoctors; }
    public Integer getActiveDoctors() { return activeDoctors; }
    public Integer getInactiveDoctors() { return inactiveDoctors; }
    public Integer getTotalPatients() { return totalPatients; }
    public Integer getActivePatients() { return activePatients; }
    public Integer getNewPatientsThisMonth() { return newPatientsThisMonth; }
    public Integer getTotalAppointments() { return totalAppointments; }
    public Integer getPendingAppointments() { return pendingAppointments; }
    public Integer getCompletedAppointments() { return completedAppointments; }
    public Integer getCancelledAppointments() { return cancelledAppointments; }
    public Integer getTodayAppointments() { return todayAppointments; }
    public Integer getCompletedAppointmentsThisMonth() { return completedAppointmentsThisMonth; }
    public Integer getActiveAlerts() { return activeAlerts; }
    public Integer getCriticalAlerts() { return criticalAlerts; }
    public Integer getHighPriorityAlerts() { return highPriorityAlerts; }
    public String getSubscriptionStatus() { return subscriptionStatus; }
    public String getSubscriptionPlan() { return subscriptionPlan; }
    public LocalDateTime getSubscriptionExpiresAt() { return subscriptionExpiresAt; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public BigDecimal getMonthlyRevenue() { return monthlyRevenue; }
    public QuotaUsage getQuotaUsage() { return quotaUsage; }
}
