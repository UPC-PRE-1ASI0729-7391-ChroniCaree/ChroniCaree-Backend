/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.threads.application.services;

import com.example.threads.domain.model.Thread;
import com.example.threads.domain.repository.ThreadRepository;
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

    public Optional<Thread> getThreadById(String id) {
        return threadRepository.findById(id);
    }

    public List<Thread> getThreadsByPatientId(String patientId) {
        return threadRepository.findByPatientId(patientId);
    }

    public Thread createThread(Thread thread) {
        return threadRepository.save(thread);
    }

    public Thread updateThread(String id, Thread updatedThread) {
        return threadRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(updatedThread.getStatus());
                    existing.setSubject(updatedThread.getSubject());
                    existing.setHasUrgentMessages(updatedThread.getHasUrgentMessages());
                    existing.setUnreadCount(updatedThread.getUnreadCount());
                    existing.setUpdatedAt(updatedThread.getUpdatedAt());
                    return threadRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Thread not found with id: " + id));
    }

    public void deleteThread(String id) {
        threadRepository.deleteById(id);
    }
}
