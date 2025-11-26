/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.infrastructure.persistence;

import com.chronicare.platform.medication.domain.aggregate.MedicationLog;
import com.chronicare.platform.medication.domain.repository.MedicationLogRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMedicationLogRepository extends JpaRepository<MedicationLog, Long>, MedicationLogRepository {

    @Override
    List<MedicationLog> findByMedicationId(Long medicationId);
}

