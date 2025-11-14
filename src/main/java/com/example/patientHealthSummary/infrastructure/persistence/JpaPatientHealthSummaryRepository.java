/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.patientHealthSummary.infrastructure.persistence;

import com.example.patientHealthSummary.domain.model.PatientHealthSummary;
import com.example.patientHealthSummary.domain.repository.PatientHealthSummaryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPatientHealthSummaryRepository extends JpaRepository<PatientHealthSummary, Long>, PatientHealthSummaryRepository {

    @Override
    List<PatientHealthSummary> findAll();

    @Override
    PatientHealthSummary findByUserId(Long userId);

    @Override
    List<PatientHealthSummary> findByAssignedDoctorId(Long doctorId);
}
