package com.chronicare.platform.doctors.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Doctor Dashboard Resource
 */
public record DoctorDashboardResource(
    @JsonProperty("doctorId") Long doctorId,
    @JsonProperty("doctorName") String doctorName,
    @JsonProperty("todayAppointments") List<AppointmentDetailResource> todayAppointments,
    @JsonProperty("recentPatients") List<PatientSummaryResource> recentPatients,
    @JsonProperty("weeklySchedule") List<ScheduleEntryResource> weeklySchedule,
    @JsonProperty("totalPatientsToday") Integer totalPatientsToday,
    @JsonProperty("completedAppointmentsToday") Integer completedAppointmentsToday,
    @JsonProperty("pendingAppointmentsToday") Integer pendingAppointmentsToday,
    @JsonProperty("totalActivePatients") Integer totalActivePatients,
    @JsonProperty("criticalAlerts") Integer criticalAlerts,
    @JsonProperty("pendingTasks") Integer pendingTasks
) {}
