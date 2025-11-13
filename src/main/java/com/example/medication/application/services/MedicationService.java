/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.medication.application.services;

import com.example.medication.domain.model.Medication;
import com.example.medication.domain.repository.MedicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public Medication getMedicationById(String id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));
    }

    public List<Medication> getMedicationsByPatient(String patientId) {
        return medicationRepository.findByPatientId(patientId);
    }

    public Medication createMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication updateMedication(String id, Medication updated) {
        return medicationRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setDosage(updated.getDosage());
                    existing.setType(updated.getType());
                    existing.setSchedule(updated.getSchedule());
                    existing.setStatus(updated.getStatus());
                    existing.setInstructions(updated.getInstructions());
                    existing.setPurpose(updated.getPurpose());
                    return medicationRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));
    }

    public void deleteMedication(String id) {
        medicationRepository.deleteById(id);
    }
}