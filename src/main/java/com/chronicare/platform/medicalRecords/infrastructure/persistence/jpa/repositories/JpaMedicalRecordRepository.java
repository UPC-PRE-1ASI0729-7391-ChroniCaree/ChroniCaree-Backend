package com.chronicare.platform.medicalRecords.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.medicalRecords.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.medicalRecords.domain.repository.MedicalRecordRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA implementation of the medical record repository
 */
@Repository
public interface JpaMedicalRecordRepository extends JpaRepository<MedicalRecord, Long>, MedicalRecordRepository {

    @Override
    List<MedicalRecord> findByPatientId(Long patientId);
    
    @Override
    List<MedicalRecord> findByDoctorId(Long doctorId);
}

