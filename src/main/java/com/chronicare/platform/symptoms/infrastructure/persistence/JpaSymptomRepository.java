/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.symptoms.infrastructure.persistence;

 
import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.symptoms.domain.model.aggregates.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaSymptomRepository extends JpaRepository<Symptom, Long> {

    List<Symptom> findByPatient(Patient patient);
}
