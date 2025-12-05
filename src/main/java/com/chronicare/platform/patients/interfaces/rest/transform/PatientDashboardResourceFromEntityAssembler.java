package com.chronicare.platform.patients.interfaces.rest.transform;

import com.chronicare.platform.patients.domain.model.aggregates.PatientDashboard;
import com.chronicare.platform.patients.domain.model.valueobjects.AlertSummary;
import com.chronicare.platform.patients.domain.model.valueobjects.AppointmentSummary;
import com.chronicare.platform.patients.domain.model.valueobjects.MedicationSummary;
import com.chronicare.platform.patients.domain.model.valueobjects.VitalSignsSummary;
import com.chronicare.platform.patients.interfaces.rest.resources.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Patient Dashboard Resource Assembler
 * Transforms PatientDashboard entity to PatientDashboardResource
 */
public class PatientDashboardResourceFromEntityAssembler {
    
    public static PatientDashboardResource toResourceFromEntity(PatientDashboard dashboard) {
        return new PatientDashboardResource(
            dashboard.getPatientId(),
            dashboard.getPatientName(),
            toAppointmentResources(dashboard.getUpcomingAppointments()),
            toMedicationResources(dashboard.getActiveMedications()),
            toAlertResources(dashboard.getActiveAlerts()),
            toVitalSignsResource(dashboard.getLatestVitalSigns()),
            dashboard.getTotalAppointments(),
            dashboard.getPendingAppointments(),
            dashboard.getTotalMedications(),
            dashboard.getCriticalAlerts()
        );
    }
    
    private static List<AppointmentSummaryResource> toAppointmentResources(List<AppointmentSummary> appointments) {
        if (appointments == null) return List.of();
        return appointments.stream()
            .map(apt -> new AppointmentSummaryResource(
                apt.appointmentId(),
                apt.doctorId(),
                apt.doctorName(),
                apt.specialty(),
                apt.appointmentDateTime(),
                apt.status(),
                apt.appointmentType(),
                apt.location(),
                apt.notes()
            ))
            .collect(Collectors.toList());
    }
    
    private static List<MedicationSummaryResource> toMedicationResources(List<MedicationSummary> medications) {
        if (medications == null) return List.of();
        return medications.stream()
            .map(med -> new MedicationSummaryResource(
                med.medicationId(),
                med.medicationName(),
                med.dosage(),
                med.frequency(),
                med.startDate(),
                med.endDate(),
                med.prescribedBy(),
                med.instructions(),
                med.isActive()
            ))
            .collect(Collectors.toList());
    }
    
    private static List<AlertSummaryResource> toAlertResources(List<AlertSummary> alerts) {
        if (alerts == null) return List.of();
        return alerts.stream()
            .map(alert -> new AlertSummaryResource(
                alert.alertId(),
                alert.alertType(),
                alert.severity(),
                alert.message(),
                alert.triggeredAt(),
                alert.isRead(),
                alert.actionRequired(),
                alert.dismissedAt()
            ))
            .collect(Collectors.toList());
    }
    
    private static VitalSignsSummaryResource toVitalSignsResource(VitalSignsSummary vitalSigns) {
        if (vitalSigns == null) return null;
        return new VitalSignsSummaryResource(
            vitalSigns.recordId(),
            vitalSigns.bloodPressureSystolic(),
            vitalSigns.bloodPressureDiastolic(),
            vitalSigns.heartRate(),
            vitalSigns.temperature(),
            vitalSigns.weight(),
            vitalSigns.height(),
            vitalSigns.bmi(),
            vitalSigns.measuredAt(),
            vitalSigns.measuredBy(),
            vitalSigns.notes()
        );
    }
}
