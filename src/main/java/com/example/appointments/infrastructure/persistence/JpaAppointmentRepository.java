/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.appointments.infrastructure.persistence;

import com.example.appointments.domain.model.Appointment;
import com.example.appointments.domain.repository.AppointmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaAppointmentRepository extends JpaRepository<Appointment, Long>, AppointmentRepository {
    
    @Override
    List<Appointment> findByPatientId(Long patientId);
    
    @Override
    List<Appointment> findByDoctorId(Long doctorId);
}
