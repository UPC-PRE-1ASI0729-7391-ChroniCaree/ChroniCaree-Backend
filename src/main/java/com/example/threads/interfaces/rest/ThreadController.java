/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.threads.interfaces.rest;

import com.example.threads.application.services.ThreadService;
import com.example.threads.domain.model.Thread;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Threads.
 */
@RestController
@RequestMapping("/api/v1/threads")
@Tag(name = "Threads", description = "Thread management API")
public class ThreadController {

    private final ThreadService threadService;

    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @GetMapping
    @Operation(summary = "List all threads")
    public ResponseEntity<List<Thread>> getAllThreads() {
        return ResponseEntity.ok(threadService.getAllThreads());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get thread by ID")
    public ResponseEntity<Thread> getThreadById(@PathVariable Long id) {
        Optional<Thread> oThread = threadService.getThreadById(id);
        if (oThread.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(oThread.get());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get threads by patient ID")
    public ResponseEntity<List<Thread>> getThreadsByPatientId(@PathVariable String patientId) {
        return ResponseEntity.ok(threadService.getThreadsByPatientId(patientId));
    }

    @PostMapping
    @Operation(summary = "Create new thread")
    public ResponseEntity<Thread> createThread(@RequestBody Thread thread) {
        Thread created = threadService.createThread(thread);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update thread by ID")
    public ResponseEntity<Thread> updateThread(@PathVariable Long id, @RequestBody Thread thread) {
        Optional<Thread> oThread = threadService.getThreadById(id);
        if (oThread.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(threadService.updateThread(id, thread));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete thread by ID")
    public ResponseEntity<Void> deleteThread(@PathVariable Long id) {
        Optional<Thread> oThread = threadService.getThreadById(id);
        if (oThread.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        threadService.deleteThread(id);
        return ResponseEntity.noContent().build();
    }
}
