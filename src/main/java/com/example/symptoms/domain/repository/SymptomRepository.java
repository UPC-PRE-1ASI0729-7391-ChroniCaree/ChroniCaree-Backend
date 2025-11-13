/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.symptoms.domain.repository;

import com.example.symptoms.domain.model.Symptom;
import java.util.List;
import java.util.Optional;

public interface SymptomRepository {

    List<Symptom> findAll();

    Optional<Symptom> findById(Long id);

    List<Symptom> findByPatientId(Long patientId);

    Symptom save(Symptom symptom);

    void deleteById(Long id);
}
