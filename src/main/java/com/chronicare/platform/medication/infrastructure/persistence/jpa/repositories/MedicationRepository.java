package com.chronicare.platform.medication.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.medication.domain.model.aggregates.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Medication aggregate
 */
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByPatientId(String patientId);
}
