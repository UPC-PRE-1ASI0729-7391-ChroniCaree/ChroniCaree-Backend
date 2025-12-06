package com.chronicare.platform.appointments.application.service;

import com.chronicare.platform.appointments.domain.model.aggregates.Appointment;
import com.chronicare.platform.appointments.domain.model.valueobjects.AppointmentType;
import com.chronicare.platform.appointments.domain.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public UUID createAppointment(String tenantId, String patientId, String doctorId,
                                 LocalDateTime startAt, LocalDateTime endAt, Integer durationMinutes,
                                 AppointmentType type, String createdBy) {
        List<Appointment> overlapping = appointmentRepository.findByDoctorAndDateRange(
            doctorId, startAt, endAt, tenantId
        );
        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("Doctor has overlapping appointments");
        }

        Appointment appointment = new Appointment(tenantId, patientId, doctorId,
            startAt, endAt, durationMinutes, type, createdBy);
        
        appointmentRepository.save(appointment);
        return appointment.getAppointmentId();
    }

    public Optional<Appointment> getAppointment(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    public List<Appointment> getAppointmentsByPatient(String patientId, String tenantId) {
        return appointmentRepository.findByPatientId(patientId, tenantId);
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorId, String tenantId) {
        return appointmentRepository.findByDoctorId(doctorId, tenantId);
    }

    public void confirmAppointment(UUID appointmentId, String tenantId) {
        Optional<Appointment> app = appointmentRepository.findById(appointmentId);
        if (app.isPresent()) {
            Appointment appointment = app.get();
            if (!appointment.getTenantId().equals(tenantId)) {
                throw new IllegalArgumentException("Tenant mismatch");
            }
            appointment.confirm();
            appointmentRepository.save(appointment);
        }
    }

    public void completeAppointment(UUID appointmentId, String tenantId) {
        Optional<Appointment> app = appointmentRepository.findById(appointmentId);
        if (app.isPresent()) {
            Appointment appointment = app.get();
            if (!appointment.getTenantId().equals(tenantId)) {
                throw new IllegalArgumentException("Tenant mismatch");
            }
            appointment.complete();
            appointmentRepository.save(appointment);
        }
    }

    public void cancelAppointment(UUID appointmentId, String tenantId, String reason) {
        Optional<Appointment> app = appointmentRepository.findById(appointmentId);
        if (app.isPresent()) {
            Appointment appointment = app.get();
            if (!appointment.getTenantId().equals(tenantId)) {
                throw new IllegalArgumentException("Tenant mismatch");
            }
            if (!appointment.canBeCancelled()) {
                throw new IllegalStateException("Cannot cancel appointment in " + appointment.getStatus() + " status");
            }
            appointment.cancel(reason);
            appointmentRepository.save(appointment);
        }
    }
}
