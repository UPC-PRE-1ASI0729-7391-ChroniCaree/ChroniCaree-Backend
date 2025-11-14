/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patients.application.services;

import com.example.patients.domain.model.Patient;
import com.example.patients.domain.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> getPatientByDni(String dni) {
        return patientRepository.findByDni(dni);
    }

    public Patient createPatient(Patient patient) {
        patientRepository.findByDni(patient.getDni()).ifPresent(existing -> {
            throw new RuntimeException("A patient with DNI " + patient.getDni() + " already exists.");
        });

        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        updatedPatient.setId(id);
        return patientRepository.save(updatedPatient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
