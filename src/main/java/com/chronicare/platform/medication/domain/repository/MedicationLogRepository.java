/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.repository;

import com.chronicare.platform.medication.domain.aggregate.MedicationLog;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Barturen
 */
public interface MedicationLogRepository {

    List<MedicationLog> findAll();

    Optional<MedicationLog> findById(Long id);

    List<MedicationLog> findByMedicationId(Long medicationId);

    MedicationLog save(MedicationLog log);

    void deleteById(Long id);
}
