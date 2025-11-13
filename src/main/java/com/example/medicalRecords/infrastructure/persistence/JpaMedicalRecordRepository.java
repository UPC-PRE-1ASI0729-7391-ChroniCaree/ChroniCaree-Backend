/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.medicalRecords.infrastructure.persistence;

import com.example.medicalRecords.domain.model.MedicalRecord;
import com.example.medicalRecords.domain.repository.MedicalRecordRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Implementación JPA del repositorio de registros médicos.
 */
@Repository
public interface JpaMedicalRecordRepository extends JpaRepository<MedicalRecord, Long>, MedicalRecordRepository {

    @Override
    List<MedicalRecord> findByPatientId(Long patientId);
    
    @Override
    List<MedicalRecord> findByDoctorId(Long doctorId);
}
