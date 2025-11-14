/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.patientHealthSummary.domain.repository;

import com.example.patientHealthSummary.domain.model.PatientHealthSummary;
import java.util.List;
import java.util.Optional;

public interface PatientHealthSummaryRepository {

    List<PatientHealthSummary> findAll();

    Optional<PatientHealthSummary> findById(Long id);

    PatientHealthSummary save(PatientHealthSummary summary);

    PatientHealthSummary findByUserId(Long userId);

    List<PatientHealthSummary> findByAssignedDoctorId(Long doctorId); 
        void deleteById
        (Long id
    

);
}
