package com.chronicare.platform.tenants.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Dashboard Stats Resource for REST API responses
 * Contains comprehensive tenant statistics
 */
public record DashboardStatsResource(
    @JsonProperty("tenantId") Long tenantId,
    @JsonProperty("tenantName") String tenantName,
    @JsonProperty("totalDoctors") Integer totalDoctors,
    @JsonProperty("activeDoctors") Integer activeDoctors,
    @JsonProperty("inactiveDoctors") Integer inactiveDoctors,
    @JsonProperty("totalPatients") Integer totalPatients,
    @JsonProperty("activePatients") Integer activePatients,
    @JsonProperty("newPatientsThisMonth") Integer newPatientsThisMonth,
    @JsonProperty("totalAppointments") Integer totalAppointments,
    @JsonProperty("pendingAppointments") Integer pendingAppointments,
    @JsonProperty("completedAppointments") Integer completedAppointments,
    @JsonProperty("cancelledAppointments") Integer cancelledAppointments,
    @JsonProperty("todayAppointments") Integer todayAppointments,
    @JsonProperty("completedAppointmentsThisMonth") Integer completedAppointmentsThisMonth,
    @JsonProperty("activeAlerts") Integer activeAlerts,
    @JsonProperty("criticalAlerts") Integer criticalAlerts,
    @JsonProperty("highPriorityAlerts") Integer highPriorityAlerts,
    @JsonProperty("subscriptionStatus") String subscriptionStatus,
    @JsonProperty("subscriptionPlan") String subscriptionPlan,
    @JsonProperty("subscriptionExpiresAt") LocalDateTime subscriptionExpiresAt,
    @JsonProperty("totalRevenue") BigDecimal totalRevenue,
    @JsonProperty("monthlyRevenue") BigDecimal monthlyRevenue,
    @JsonProperty("quotaUsage") QuotaUsageResource quotaUsage
) {
}
