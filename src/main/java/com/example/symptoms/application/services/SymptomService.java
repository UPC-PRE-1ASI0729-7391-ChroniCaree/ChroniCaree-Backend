/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.symptoms.application.services;

import com.example.symptoms.domain.aggregate.Symptom;
import com.example.symptoms.domain.commands.CreateSymptomCommand;
import com.example.symptoms.domain.commands.UpdateSymptomCommand;
import com.example.symptoms.infrastructure.persistence.JpaSymptomRepository;
import com.example.symptoms.domain.valueobject.PatientId;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SymptomService {

    private final JpaSymptomRepository repository;

    public SymptomService(JpaSymptomRepository repository) {
        this.repository = repository;
    }

    public List<Symptom> getAllSymptoms() {
        return repository.findAll();
    }

    public Optional<Symptom> getSymptomById(Long id) {
        return repository.findById(id);
    }

    public List<Symptom> getSymptomsByPatientId(Long patientId) {
        return repository.findByPatientId(new PatientId(patientId));
    }

    public Symptom createSymptom(CreateSymptomCommand command) {
        Symptom symptom = new Symptom(command);
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
