/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.medication.infrastructure.persistence;

import com.chronicare.platform.medication.domain.aggregate.Medication;
import com.chronicare.platform.medication.domain.repository.MedicationRepository;
import com.chronicare.platform.medication.domain.valueobject.MedicationStatusVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaMedicationRepository extends JpaRepository<Medication, Long>, MedicationRepository {

    @Override
    List<Medication> findByPatientId(Long patientId);

    @Override
    List<Medication> findByStatus(MedicationStatusVO status);
}