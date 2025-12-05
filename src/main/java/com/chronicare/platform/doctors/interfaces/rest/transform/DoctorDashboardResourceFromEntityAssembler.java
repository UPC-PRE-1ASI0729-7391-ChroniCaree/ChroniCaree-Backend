package com.chronicare.platform.doctors.interfaces.rest.transform;

import com.chronicare.platform.doctors.domain.model.aggregates.DoctorDashboard;
import com.chronicare.platform.doctors.domain.model.valueobjects.AppointmentDetail;
import com.chronicare.platform.doctors.domain.model.valueobjects.PatientSummary;
import com.chronicare.platform.doctors.domain.model.valueobjects.ScheduleEntry;
import com.chronicare.platform.doctors.interfaces.rest.resources.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Doctor Dashboard Resource Assembler
 * Transforms DoctorDashboard entity to DoctorDashboardResource
 */
public class DoctorDashboardResourceFromEntityAssembler {
    
    public static DoctorDashboardResource toResourceFromEntity(DoctorDashboard dashboard) {
        return new DoctorDashboardResource(
            dashboard.getDoctorId(),
            dashboard.getDoctorName(),
            toAppointmentResources(dashboard.getTodayAppointments()),
            toPatientResources(dashboard.getRecentPatients()),
            toScheduleResources(dashboard.getWeeklySchedule()),
            dashboard.getTotalPatientsToday(),
            dashboard.getCompletedAppointmentsToday(),
            dashboard.getPendingAppointmentsToday(),
            dashboard.getTotalActivePatients(),
            dashboard.getCriticalAlerts(),
            dashboard.getPendingTasks()
        );
    }
    
    private static List<AppointmentDetailResource> toAppointmentResources(List<AppointmentDetail> appointments) {
        if (appointments == null) return List.of();
        return appointments.stream()
            .map(apt -> new AppointmentDetailResource(
                apt.appointmentId(),
                apt.patientId(),
                apt.patientName(),
                apt.patientAge(),
                apt.appointmentDateTime(),
                apt.status(),
                apt.appointmentType(),
                apt.reason(),
                apt.location(),
                apt.notes(),
                apt.isUrgent()
            ))
            .collect(Collectors.toList());
    }
    
    private static List<PatientSummaryResource> toPatientResources(List<PatientSummary> patients) {
        if (patients == null) return List.of();
        return patients.stream()
            .map(pat -> new PatientSummaryResource(
                pat.patientId(),
                pat.patientName(),
                pat.age(),
                pat.gender(),
                pat.bloodType(),
                pat.contactPhone(),
                pat.lastVisit(),
                pat.totalVisits(),
                pat.hasActiveAlerts(),
                pat.criticalAlerts(),
                pat.currentCondition()
            ))
            .collect(Collectors.toList());
    }
    
    private static List<ScheduleEntryResource> toScheduleResources(List<ScheduleEntry> schedule) {
        if (schedule == null) return List.of();
        return schedule.stream()
            .map(sch -> new ScheduleEntryResource(
                sch.dayOfWeek(),
                sch.startTime(),
                sch.endTime(),
                sch.appointmentDuration(),
                sch.isAvailable()
            ))
            .collect(Collectors.toList());
    }
}
