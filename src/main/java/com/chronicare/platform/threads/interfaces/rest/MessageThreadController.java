package com.chronicare.platform.threads.interfaces.rest;

import com.chronicare.platform.threads.application.services.ThreadService;
import com.chronicare.platform.threads.domain.model.Thread;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Alias controller for message threads with frontend-expected URL
 * Frontend expects /api/v1/message-threads
 */
@RestController
@RequestMapping("/api/v1/message-threads")
@Tag(name = "Message Threads (Alias)", description = "Message Thread management API - Alias for frontend compatibility")
public class MessageThreadController {

    private final ThreadService threadService;

    public MessageThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @GetMapping
    @Operation(summary = "List all message threads")
    public ResponseEntity<List<Thread>> getAllThreads() {
        return ResponseEntity.ok(threadService.getAllThreads());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get message thread by ID")
    public ResponseEntity<Thread> getThreadById(@PathVariable Long id) {
        Optional<Thread> oThread = threadService.getThreadById(id);
        if (oThread.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(oThread.get());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get message threads by patient ID")
    public ResponseEntity<List<Thread>> getThreadsByPatientId(@PathVariable String patientId) {
        return ResponseEntity.ok(threadService.getThreadsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get message threads by doctor ID")
    public ResponseEntity<List<Thread>> getThreadsByDoctorId(@PathVariable String doctorId) {
        return ResponseEntity.ok(threadService.getThreadsByDoctorId(doctorId));
    }

    @PostMapping
    @Operation(summary = "Create new message thread")
    public ResponseEntity<Thread> createThread(@RequestBody Thread thread) {
        Thread created = threadService.createThread(thread);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update message thread by ID")
    public ResponseEntity<Thread> updateThread(@PathVariable Long id, @RequestBody Thread thread) {
        Optional<Thread> oThread = threadService.getThreadById(id);
        if (oThread.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(threadService.updateThread(id, thread));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete message thread by ID")
    public ResponseEntity<Void> deleteThread(@PathVariable Long id) {
        Optional<Thread> oThread = threadService.getThreadById(id);
        if (oThread.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        threadService.deleteThread(id);
        return ResponseEntity.noContent().build();
    }
}
