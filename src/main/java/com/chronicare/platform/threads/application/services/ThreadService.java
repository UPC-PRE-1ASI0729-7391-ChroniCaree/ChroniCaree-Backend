/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.threads.application.services;

import com.chronicare.platform.threads.domain.model.Thread;
import com.chronicare.platform.threads.domain.repository.ThreadRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para la gestión de Threads.
 */
@Service
@Transactional
public class ThreadService {
    
    private final ThreadRepository threadRepository;
    
    public ThreadService(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }
    
    public List<Thread> getAllThreads() {
        return threadRepository.findAll();
    }
    
    public Optional<Thread> getThreadById(Long id) {
        return threadRepository.findById(id);
    }
    
    public List<Thread> getThreadsByPatientId(String patientId) {
        return threadRepository.findByPatientId(patientId);
    }

    public List<Thread> getThreadsByDoctorId(String doctorId) {
        return threadRepository.findByDoctorId(doctorId);
    }
    
    public Thread createThread(Thread thread) {
        return threadRepository.save(thread);
    }
    
    public Thread updateThread(Long id, Thread updatedThread) {
        updatedThread.setId(id);
        return threadRepository.save(updatedThread);
    }
    
    public void deleteThread(Long id) {
        threadRepository.deleteById(id);
    }
}
