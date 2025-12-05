package com.chronicare.platform.patients.domain.model.aggregates;

import com.chronicare.platform.patients.domain.model.valueobjects.AlertSummary;
import com.chronicare.platform.patients.domain.model.valueobjects.AppointmentSummary;
import com.chronicare.platform.patients.domain.model.valueobjects.MedicationSummary;
import com.chronicare.platform.patients.domain.model.valueobjects.VitalSignsSummary;

import java.util.List;

/**
 * Patient Dashboard Aggregate Root
 * Encapsulates all patient dashboard information
 */
public class PatientDashboard {
    
    private final Long patientId;
    private final String patientName;
    private final List<AppointmentSummary> upcomingAppointments;
    private final List<MedicationSummary> activeMedications;
    private final List<AlertSummary> activeAlerts;
    private final VitalSignsSummary latestVitalSigns;
    private final Integer totalAppointments;
    private final Integer pendingAppointments;
    private final Integer totalMedications;
    private final Integer criticalAlerts;
    
    public PatientDashboard(
        Long patientId,
        String patientName,
        List<AppointmentSummary> upcomingAppointments,
        List<MedicationSummary> activeMedications,
        List<AlertSummary> activeAlerts,
        VitalSignsSummary latestVitalSigns,
        Integer totalAppointments,
        Integer pendingAppointments,
        Integer totalMedications,
        Integer criticalAlerts
    ) {
        if (patientId == null) throw new IllegalArgumentException("Patient ID cannot be null");
        if (patientName == null || patientName.isBlank()) throw new IllegalArgumentException("Patient name cannot be null or blank");
        
        this.patientId = patientId;
        this.patientName = patientName;
        this.upcomingAppointments = upcomingAppointments != null ? upcomingAppointments : List.of();
        this.activeMedications = activeMedications != null ? activeMedications : List.of();
        this.activeAlerts = activeAlerts != null ? activeAlerts : List.of();
        this.latestVitalSigns = latestVitalSigns;
        this.totalAppointments = totalAppointments != null ? totalAppointments : 0;
        this.pendingAppointments = pendingAppointments != null ? pendingAppointments : 0;
        this.totalMedications = totalMedications != null ? totalMedications : 0;
        this.criticalAlerts = criticalAlerts != null ? criticalAlerts : 0;
    }
    
    public Long getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public List<AppointmentSummary> getUpcomingAppointments() { return upcomingAppointments; }
    public List<MedicationSummary> getActiveMedications() { return activeMedications; }
    public List<AlertSummary> getActiveAlerts() { return activeAlerts; }
    public VitalSignsSummary getLatestVitalSigns() { return latestVitalSigns; }
    public Integer getTotalAppointments() { return totalAppointments; }
    public Integer getPendingAppointments() { return pendingAppointments; }
    public Integer getTotalMedications() { return totalMedications; }
    public Integer getCriticalAlerts() { return criticalAlerts; }
    
    public boolean hasUpcomingAppointments() {
        return !upcomingAppointments.isEmpty();
    }
    
    public boolean hasActiveMedications() {
        return !activeMedications.isEmpty();
    }
    
    public boolean hasActiveAlerts() {
        return !activeAlerts.isEmpty();
    }
    
    public boolean hasCriticalAlerts() {
        return criticalAlerts > 0;
    }
    
    public boolean hasVitalSigns() {
        return latestVitalSigns != null;
    }
}
