package com.chronicare.platform.doctors.domain.model.aggregates;

import com.chronicare.platform.doctors.domain.model.valueobjects.AppointmentDetail;
import com.chronicare.platform.doctors.domain.model.valueobjects.PatientSummary;
import com.chronicare.platform.doctors.domain.model.valueobjects.ScheduleEntry;

import java.util.List;

/**
 * Doctor Dashboard Aggregate Root
 * Encapsulates all doctor dashboard information
 */
public class DoctorDashboard {
    
    private final Long doctorId;
    private final String doctorName;
    private final List<AppointmentDetail> todayAppointments;
    private final List<PatientSummary> recentPatients;
    private final List<ScheduleEntry> weeklySchedule;
    private final Integer totalPatientsToday;
    private final Integer completedAppointmentsToday;
    private final Integer pendingAppointmentsToday;
    private final Integer totalActivePatients;
    private final Integer criticalAlerts;
    private final Integer pendingTasks;
    
    public DoctorDashboard(
        Long doctorId,
        String doctorName,
        List<AppointmentDetail> todayAppointments,
        List<PatientSummary> recentPatients,
        List<ScheduleEntry> weeklySchedule,
        Integer totalPatientsToday,
        Integer completedAppointmentsToday,
        Integer pendingAppointmentsToday,
        Integer totalActivePatients,
        Integer criticalAlerts,
        Integer pendingTasks
    ) {
        if (doctorId == null) throw new IllegalArgumentException("Doctor ID cannot be null");
        if (doctorName == null || doctorName.isBlank()) throw new IllegalArgumentException("Doctor name cannot be null or blank");
        
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.todayAppointments = todayAppointments != null ? todayAppointments : List.of();
        this.recentPatients = recentPatients != null ? recentPatients : List.of();
        this.weeklySchedule = weeklySchedule != null ? weeklySchedule : List.of();
        this.totalPatientsToday = totalPatientsToday != null ? totalPatientsToday : 0;
        this.completedAppointmentsToday = completedAppointmentsToday != null ? completedAppointmentsToday : 0;
        this.pendingAppointmentsToday = pendingAppointmentsToday != null ? pendingAppointmentsToday : 0;
        this.totalActivePatients = totalActivePatients != null ? totalActivePatients : 0;
        this.criticalAlerts = criticalAlerts != null ? criticalAlerts : 0;
        this.pendingTasks = pendingTasks != null ? pendingTasks : 0;
    }
    
    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public List<AppointmentDetail> getTodayAppointments() { return todayAppointments; }
    public List<PatientSummary> getRecentPatients() { return recentPatients; }
    public List<ScheduleEntry> getWeeklySchedule() { return weeklySchedule; }
    public Integer getTotalPatientsToday() { return totalPatientsToday; }
    public Integer getCompletedAppointmentsToday() { return completedAppointmentsToday; }
    public Integer getPendingAppointmentsToday() { return pendingAppointmentsToday; }
    public Integer getTotalActivePatients() { return totalActivePatients; }
    public Integer getCriticalAlerts() { return criticalAlerts; }
    public Integer getPendingTasks() { return pendingTasks; }
    
    public boolean hasTodayAppointments() {
        return !todayAppointments.isEmpty();
    }
    
    public boolean hasCriticalAlerts() {
        return criticalAlerts > 0;
    }
    
    public boolean hasPendingTasks() {
        return pendingTasks > 0;
    }
}
