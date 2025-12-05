package com.chronicare.platform.appointments.interfaces.rest.transform;

import com.chronicare.platform.appointments.domain.model.aggregates.Appointment;
import com.chronicare.platform.appointments.interfaces.rest.resources.AppointmentResource;

/**
 * Appointment Resource Assembler
 * Transforms Appointment entity to AppointmentResource
 */
public class AppointmentResourceFromEntityAssembler {
    
    public static AppointmentResource toResourceFromEntity(Appointment appointment) {
        return new AppointmentResource(
            appointment.getId(),
            appointment.getPatientId(),
            appointment.getDoctorId(),
            appointment.getDate(),
            appointment.getTime(),
            appointment.getType() != null ? appointment.getType().name() : null,
            appointment.getStatus() != null ? appointment.getStatus().name() : null,
            appointment.getNotes(),
            appointment.getPatientName(),
            appointment.getPatientPhone(),
            appointment.getPatientEmail(),
            appointment.getDuration()
        );
    }
}
