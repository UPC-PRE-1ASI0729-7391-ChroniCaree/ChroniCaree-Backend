/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.medicalRecords.domain.repository;

import com.example.medicalRecords.domain.model.MedicalRecord;
import java.util.List;
import java.util.Optional;

 
public interface MedicalRecordRepository {

    List<MedicalRecord> findAll();

    Optional<MedicalRecord> findById(Long id);

    List<MedicalRecord> findByPatientId(Long patientId);

    List<MedicalRecord> findByDoctorId(Long doctorId);

    MedicalRecord save(MedicalRecord medicalRecord);

    void deleteById(Long id);
}
