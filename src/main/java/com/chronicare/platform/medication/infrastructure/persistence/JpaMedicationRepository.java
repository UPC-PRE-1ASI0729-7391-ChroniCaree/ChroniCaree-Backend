/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.medication.infrastructure.persistence;

import com.chronicare.platform.medication.domain.model.Medication;
import com.chronicare.platform.medication.domain.model.MedicationStatus;
import com.chronicare.platform.medication.domain.repository.MedicationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaMedicationRepository extends JpaRepository<Medication, String>, MedicationRepository {

    @Override
    List<Medication> findByPatientId(String patientId);

    @Override
    List<Medication> findByStatus(MedicationStatus status);
}
