/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.patients.infrastructure.persistence;

import com.example.patients.domain.model.Patient;
import com.example.patients.domain.repository.PatientRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

 
@Repository
public interface JpaPatientRepository extends JpaRepository<Patient, Long>, PatientRepository {
    
    @Override
    Optional<Patient> findByDni(String dni);
}
