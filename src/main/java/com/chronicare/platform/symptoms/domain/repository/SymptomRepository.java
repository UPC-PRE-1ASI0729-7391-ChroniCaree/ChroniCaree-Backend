/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.symptoms.domain.repository;

import com.chronicare.platform.symptoms.domain.aggregate.Symptom;
import com.chronicare.platform.patients.domain.aggregates.Patient;
import java.util.List;
import java.util.Optional;

public interface SymptomRepository {

    List<Symptom> findAll();

    Optional<Symptom> findById(Long id);

    List<Symptom> findByPatient(Patient patient);

    Symptom save(Symptom symptom);

    void deleteById(Long id);
}
