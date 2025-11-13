/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.diagnosis.application.services;

import com.example.diagnosis.domain.model.Diagnosis;
import com.example.diagnosis.domain.model.DiagnosisStatus;
import com.example.diagnosis.domain.repository.DiagnosisRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    public List<Diagnosis> getAllDiagnoses() {
        return diagnosisRepository.findAll();
    }

    public Diagnosis getDiagnosisById(Long id) {
        return diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + id));
    }

    public List<Diagnosis> getDiagnosesByPatient(Long patientId) {
        return diagnosisRepository.findByPatientId(patientId);
    }

    public List<Diagnosis> getDiagnosesByDoctor(Long doctorId) {
        return diagnosisRepository.findByDoctorId(doctorId);
    }

    public List<Diagnosis> getDiagnosesByStatus(DiagnosisStatus status) {
        return diagnosisRepository.findByStatus(status);
    }

    public Diagnosis createDiagnosis(Diagnosis diagnosis) {
        return diagnosisRepository.save(diagnosis);
    }

    public Diagnosis updateDiagnosis(Long id, Diagnosis updated) {
        return diagnosisRepository.findById(id)
                .map(existing -> {
                    existing.setDiagnosisName(updated.getDiagnosisName());
                    existing.setSeverity(updated.getSeverity());
                    existing.setStatus(updated.getStatus());
                    existing.setTreatment(updated.getTreatment());
                    existing.setNotes(updated.getNotes());
                    existing.setResolvedDate(updated.getResolvedDate());
                    existing.setLastReviewDate(updated.getLastReviewDate());
                    existing.setFollowUpRequired(updated.isFollowUpRequired());
                    return diagnosisRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + id));
    }

    public void deleteDiagnosis(Long id) {
        diagnosisRepository.deleteById(id);
    }
}
