/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.patientHealthSummary.application.services;

import com.chronicare.platform.patientHealthSummary.domain.model.PatientHealthSummary;
import com.chronicare.platform.patientHealthSummary.domain.repository.PatientHealthSummaryRepository;
import com.chronicare.platform.patientHealthSummary.infrastructure.persistence.JpaPatientHealthSummaryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * PatientHealthSummaryService
 *
 * @summary
 * Application service responsible for orchestrating PatientHealthSummary operations.
 *
 * @description
 * - Provides a transactional boundary for CRUD operations.
 * - Delegates persistence to the domain-driven PatientHealthSummaryRepository.
 * - Supports queries by summary ID, user ID, and assigned doctor ID.
 * - Ensures correct update behavior by enforcing ID assignment on modifications.
 * This service acts as an application-level façade, encapsulating domain interactions
 * and exposing a clean API for controllers or command handlers.
 */


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
