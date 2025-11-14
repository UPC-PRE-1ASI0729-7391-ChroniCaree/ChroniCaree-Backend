/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.threads.domain.repository;

import com.example.threads.domain.model.Thread;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio del dominio para la gestión de Threads.
 */
public interface ThreadRepository {

    List<Thread> findAll();

    Optional<Thread> findById(Long id);

    List<Thread> findByPatientId(String patientId);

    Thread save(Thread thread);

    void deleteById(Long id);
}
