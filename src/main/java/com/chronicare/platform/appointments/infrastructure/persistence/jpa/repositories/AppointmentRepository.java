package com.chronicare.platform.appointments.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.appointments.domain.model.aggregates.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByDoctorIdAndDate(Long doctorId, String date);

    Optional<Appointment> findById(Long id);
}
