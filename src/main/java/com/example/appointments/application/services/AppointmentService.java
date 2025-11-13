/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.appointments.application.services;

import com.example.appointments.domain.model.Appointment;
import com.example.appointments.domain.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

 
@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        return appointmentRepository.findById(id)
                .map(existing -> {
                    existing.setDate(updatedAppointment.getDate());
                    existing.setTime(updatedAppointment.getTime());
                    existing.setType(updatedAppointment.getType());
                    existing.setStatus(updatedAppointment.getStatus());
                    existing.setNotes(updatedAppointment.getNotes());
                    existing.setPatientName(updatedAppointment.getPatientName());
                    existing.setPatientPhone(updatedAppointment.getPatientPhone());
                    existing.setPatientEmail(updatedAppointment.getPatientEmail());
                    existing.setDuration(updatedAppointment.getDuration());
                    return appointmentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
