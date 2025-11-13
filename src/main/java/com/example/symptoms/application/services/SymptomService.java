/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.symptoms.application.services;

import com.example.symptoms.domain.model.Symptom;
import com.example.symptoms.domain.repository.SymptomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SymptomService {

    private final SymptomRepository symptomRepository;

    public SymptomService(SymptomRepository symptomRepository) {
        this.symptomRepository = symptomRepository;
    }

    public List<Symptom> getAllSymptoms() {
        return symptomRepository.findAll();
    }

    public Optional<Symptom> getSymptomById(Long id) {
        return symptomRepository.findById(id);
    }

    public List<Symptom> getSymptomsByPatientId(Long patientId) {
        return symptomRepository.findByPatientId(patientId);
    }

    public Symptom createSymptom(Symptom symptom) {
        return symptomRepository.save(symptom);
    }

    public Symptom updateSymptom(Long id, Symptom updatedSymptom) {
        return symptomRepository.findById(id)
                .map(existing -> {
                    existing.setGlucose(updatedSymptom.getGlucose());
                    existing.setBloodPressure(updatedSymptom.getBloodPressure());
                    existing.setHeartRate(updatedSymptom.getHeartRate());
                    existing.setTemperature(updatedSymptom.getTemperature());
                    existing.setOxygenSaturation(updatedSymptom.getOxygenSaturation());
                    existing.setFatigue(updatedSymptom.getFatigue());
                    existing.setPain(updatedSymptom.getPain());
                    existing.setDizziness(updatedSymptom.getDizziness());
                    existing.setNotes(updatedSymptom.getNotes());
                    existing.setIsEdited(true);
                    existing.setEditedAt(updatedSymptom.getEditedAt());
                    return symptomRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Symptom not found with id: " + id));
    }

    public void deleteSymptom(Long id) {
        symptomRepository.deleteById(id);
    }
}
