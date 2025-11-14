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
import java.util.Optional;

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

    public Optional<Medication> getMedicationById(Long id) {
        return medicationRepository.findById(id);

    }

    public List<Medication> getMedicationsByPatient(String patientId) {
        return medicationRepository.findByPatientId(patientId);
    }

    public Medication createMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication updateMedication(Long id, Medication updated) {
        updated.setId(id);
        return medicationRepository.save(updated);
    }

    public void deleteMedication(Long id) {
        medicationRepository.deleteById(id);
    }
}
