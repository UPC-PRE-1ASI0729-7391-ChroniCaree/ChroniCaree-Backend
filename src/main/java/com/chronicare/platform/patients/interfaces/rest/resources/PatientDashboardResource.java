package com.chronicare.platform.patients.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Patient Dashboard Resource
 */
public record PatientDashboardResource(
    @JsonProperty("patientId") Long patientId,
    @JsonProperty("patientName") String patientName,
    @JsonProperty("upcomingAppointments") List<AppointmentSummaryResource> upcomingAppointments,
    @JsonProperty("activeMedications") List<MedicationSummaryResource> activeMedications,
    @JsonProperty("activeAlerts") List<AlertSummaryResource> activeAlerts,
    @JsonProperty("latestVitalSigns") VitalSignsSummaryResource latestVitalSigns,
    @JsonProperty("totalAppointments") Integer totalAppointments,
    @JsonProperty("pendingAppointments") Integer pendingAppointments,
    @JsonProperty("totalMedications") Integer totalMedications,
    @JsonProperty("criticalAlerts") Integer criticalAlerts
) {}
