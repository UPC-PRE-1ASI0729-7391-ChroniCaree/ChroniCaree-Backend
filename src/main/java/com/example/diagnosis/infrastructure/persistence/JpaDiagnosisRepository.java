/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.diagnosis.infrastructure.persistence;

import com.example.diagnosis.domain.model.Diagnosis;
import com.example.diagnosis.domain.model.DiagnosisSeverity;
import com.example.diagnosis.domain.model.DiagnosisStatus;
import com.example.diagnosis.domain.repository.DiagnosisRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaDiagnosisRepository extends JpaRepository<Diagnosis, Long>, DiagnosisRepository {

    @Override
    List<Diagnosis> findByPatientId(Long patientId);

    @Override
    List<Diagnosis> findByDoctorId(Long doctorId);

    @Override
    List<Diagnosis> findByStatus(DiagnosisStatus status);

    @Override
    List<Diagnosis> findBySeverity(DiagnosisSeverity severity);
}
