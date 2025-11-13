/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.appointments.domain.repository;

import com.example.appointments.domain.model.Appointment;
import java.util.List;
import java.util.Optional;


public interface AppointmentRepository {

    List<Appointment> findAll();

    Optional<Appointment> findById(Long id);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    Appointment save(Appointment appointment);

    void deleteById(Long id);

}
