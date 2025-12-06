package com.chronicare.platform.appointments.infrastructure.persistence;

import com.chronicare.platform.appointments.domain.model.aggregates.Appointment;
import com.chronicare.platform.appointments.domain.repository.AppointmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAppointmentRepository extends JpaRepository<Appointment, UUID>, AppointmentRepository {

    @Override
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.tenantId = :tenantId")
    List<Appointment> findByDoctorId(String doctorId, String tenantId);

    @Override
    @Query("SELECT a FROM Appointment a WHERE a.patientId = :patientId AND a.tenantId = :tenantId")
    List<Appointment> findByPatientId(String patientId, String tenantId);

    @Override
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.startAt >= :start AND a.endAt <= :end AND a.tenantId = :tenantId")
    List<Appointment> findByDoctorAndDateRange(String doctorId, LocalDateTime start, LocalDateTime end, String tenantId);
}
