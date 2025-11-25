/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.symptoms.infrastructure.persistence;

 
import com.chronicare.platform.patients.domain.aggregates.Patient;
import com.chronicare.platform.symptoms.domain.aggregate.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaSymptomRepository extends JpaRepository<Symptom, Long> {

    List<Symptom> findByPatient(Patient patient);
}
