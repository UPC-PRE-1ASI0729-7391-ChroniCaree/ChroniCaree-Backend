/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.symptoms.infrastructure.persistence;

import com.example.symptoms.domain.aggregate.Symptom;
import com.example.symptoms.domain.valueobject.PatientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaSymptomRepository extends JpaRepository<Symptom, Long> {
    List<Symptom> findByPatientId(PatientId patientId);
}