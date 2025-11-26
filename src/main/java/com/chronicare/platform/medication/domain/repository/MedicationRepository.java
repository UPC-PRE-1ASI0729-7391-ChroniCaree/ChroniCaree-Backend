/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.medication.domain.repository;

import com.chronicare.platform.medication.domain.aggregate.Medication;
import com.chronicare.platform.medication.domain.valueobject.MedicationStatusVO;
import java.util.List;
import java.util.Optional;

public interface MedicationRepository {

    List<Medication> findAll();

    Optional<Medication> findById(Long id);

    List<Medication> findByPatientId(Long patientId);

    List<Medication> findByStatus(MedicationStatusVO status);

    Medication save(Medication medication);

    void deleteById(Long id);
}
