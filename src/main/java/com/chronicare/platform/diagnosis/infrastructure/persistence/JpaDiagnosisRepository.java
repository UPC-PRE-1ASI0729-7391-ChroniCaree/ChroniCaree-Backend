/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.diagnosis.infrastructure.persistence;

import com.chronicare.platform.diagnosis.domain.model.Diagnosis;
import com.chronicare.platform.diagnosis.domain.model.DiagnosisSeverity;
import com.chronicare.platform.diagnosis.domain.model.DiagnosisStatus;
import com.chronicare.platform.diagnosis.domain.repository.DiagnosisRepository;
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
