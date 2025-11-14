/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patientHealthSummary.application.services;

import com.example.patientHealthSummary.domain.model.PatientHealthSummary;
import com.example.patientHealthSummary.domain.repository.PatientHealthSummaryRepository;
import com.example.patientHealthSummary.infrastructure.persistence.JpaPatientHealthSummaryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientHealthSummaryService {

    private final PatientHealthSummaryRepository repository;

    public PatientHealthSummaryService(PatientHealthSummaryRepository repository) {
        this.repository = repository;
    }

    public List<PatientHealthSummary> getAllSummaries() {
        return repository.findAll();
    }

    public Optional<PatientHealthSummary> getSummaryById(Long id) {
        return repository.findById(id);
    }

    public PatientHealthSummary getSummaryByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<PatientHealthSummary> getSummariesByDoctorId(Long doctorId) {
        return repository.findByAssignedDoctorId(doctorId);
    }

    public PatientHealthSummary createSummary(PatientHealthSummary summary) {
        return repository.save(summary);
    }

    public PatientHealthSummary updateSummary(Long id, PatientHealthSummary updated) {
        updated.setId(id);
        return repository.save(updated);

    }

    public void deleteSummary(Long id) {
        repository.deleteById(id);
    }
}
