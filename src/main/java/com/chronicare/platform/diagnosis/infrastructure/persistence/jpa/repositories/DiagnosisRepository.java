package com.chronicare.platform.diagnosis.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.diagnosis.domain.model.aggregates.Diagnosis;
import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisSeverity;
import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Diagnosis aggregate
 */
@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByPatientId(Long patientId);
    List<Diagnosis> findByDoctorId(Long doctorId);
    List<Diagnosis> findByStatus(DiagnosisStatus status);
    List<Diagnosis> findBySeverity(DiagnosisSeverity severity);
}
