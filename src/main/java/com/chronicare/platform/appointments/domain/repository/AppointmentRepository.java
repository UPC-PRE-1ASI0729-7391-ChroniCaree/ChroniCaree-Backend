package com.chronicare.platform.appointments.domain.repository;

import com.chronicare.platform.appointments.domain.model.aggregates.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);
    Optional<Appointment> findById(UUID appointmentId);
    List<Appointment> findByDoctorId(String doctorId, String tenantId);
    List<Appointment> findByPatientId(String patientId, String tenantId);
    List<Appointment> findByDoctorAndDateRange(String doctorId, LocalDateTime start, LocalDateTime end, String tenantId);
    void deleteById(UUID appointmentId);
}
