/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.medication.domain.repository;

import com.example.medication.domain.model.Medication;
import com.example.medication.domain.model.MedicationStatus;
import java.util.List;
import java.util.Optional;

public interface MedicationRepository {

    List<Medication> findAll();

    Optional<Medication> findById(Long id);

    List<Medication> findByPatientId(String patientId);

    List<Medication> findByStatus(MedicationStatus status);

    Medication save(Medication medication);

    void deleteById(Long id);
}
