/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.symptoms.application.services;

import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.repository.PatientRepository;
import com.chronicare.platform.symptoms.domain.model.aggregates.Symptom;
import com.chronicare.platform.symptoms.domain.commands.CreateSymptomCommand;
import com.chronicare.platform.symptoms.domain.commands.UpdateSymptomCommand;
import com.chronicare.platform.symptoms.infrastructure.persistence.JpaSymptomRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SymptomService {

    private final JpaSymptomRepository repository;
    private final PatientRepository patientRepository;

    public SymptomService(JpaSymptomRepository symptomRepository, PatientRepository patientRepository) {
        this.repository = symptomRepository;
        this.patientRepository = patientRepository;
    }

    public List<Symptom> getAllSymptoms() {
        return repository.findAll();
    }

    public Optional<Symptom> getSymptomById(Long id) {
        return repository.findById(id);
    }

    public List<Symptom> getSymptomsByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        return repository.findByPatient(patient);
    }

    public Symptom createSymptom(CreateSymptomCommand command) {

        Patient patient = patientRepository.findById(command.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Symptom symptom = new Symptom(command, patient);

        return repository.save(symptom);
    }

    public Symptom updateSymptom(Long id, UpdateSymptomCommand command) {
        Symptom symptom = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Symptom not found"));
        symptom.updateSymptoms(command);
        return repository.save(symptom);
    }

    public void deleteSymptom(Long id) {
        repository.deleteById(id);
    }
}
