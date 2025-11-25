/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.patients.application.services;

import com.chronicare.platform.patients.domain.aggregates.Patient;
import com.chronicare.platform.patients.domain.commands.CreatePatientCommand;
import com.chronicare.platform.patients.domain.event.PatientCreatedEvent;
import com.chronicare.platform.patients.domain.event.PatientDeletedEvent;
import com.chronicare.platform.patients.domain.event.PatientUpdatedEvent;

import com.chronicare.platform.patients.domain.repository.PatientRepository;
import com.chronicare.platform.patients.domain.valueobjects.Dni;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PatientService(PatientRepository patientRepository, ApplicationEventPublisher eventPublisher) {
        this.patientRepository = patientRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> getPatientByDni(Dni dni) {
        return patientRepository.findByDni(dni);
    }

    public Patient createPatient(CreatePatientCommand command) {
        patientRepository.findByDni(command.dni()).ifPresent(existing -> {
            throw new RuntimeException("Patient with DNI " + command.dni().value() + " already exists");
        });

        Patient patient = new Patient(command);
        Patient saved = patientRepository.save(patient);

        // Publicar evento
        eventPublisher.publishEvent(new PatientCreatedEvent(this, saved.getId(), saved.getDni()));

        return saved;
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        existing.updateFrom(updatedPatient);
        Patient saved = patientRepository.save(existing);

        // Publicar evento
        eventPublisher.publishEvent(new PatientUpdatedEvent(this, saved.getId(), saved.getDni()));

        return saved;
    }

    public void deletePatient(Long id) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patientRepository.deleteById(existing.getId());

        // Publicar evento
        eventPublisher.publishEvent(new PatientDeletedEvent(this, id));
    }
}
