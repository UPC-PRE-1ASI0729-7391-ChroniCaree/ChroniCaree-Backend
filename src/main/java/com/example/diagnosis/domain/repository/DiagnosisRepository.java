/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.diagnosis.domain.repository;

import com.example.diagnosis.domain.model.Diagnosis;
import com.example.diagnosis.domain.model.DiagnosisSeverity;
import com.example.diagnosis.domain.model.DiagnosisStatus;
import java.util.List;
import java.util.Optional;

public interface DiagnosisRepository {

    List<Diagnosis> findAll();

    Optional<Diagnosis> findById(Long id);

    List<Diagnosis> findByPatientId(Long patientId);

    List<Diagnosis> findByDoctorId(Long doctorId);

    List<Diagnosis> findByStatus(DiagnosisStatus status);

    List<Diagnosis> findBySeverity(DiagnosisSeverity severity);

    Diagnosis save(Diagnosis diagnosis);

    void deleteById(Long id);
}
