/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.threads.infrastructure.persistence;

import com.example.threads.domain.model.Thread;
import com.example.threads.domain.repository.ThreadRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Implementación JPA del repositorio de Threads.
 */
@Repository
public interface JpaThreadRepository extends JpaRepository<Thread, String>, ThreadRepository {
    
    @Override
    List<Thread> findByPatientId(String patientId);
}
