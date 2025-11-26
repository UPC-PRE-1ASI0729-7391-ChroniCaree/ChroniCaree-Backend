/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.interfaces.rest;

import com.chronicare.platform.medication.application.services.MedicationLogService;
import com.chronicare.platform.medication.domain.aggregate.MedicationLog;
import com.chronicare.platform.medication.domain.command.CreateMedicationLogCommand;
import com.chronicare.platform.medication.domain.command.UpdateMedicationLogCommand;
import com.chronicare.platform.medication.domain.queries.GetLogByIdQuery;
import com.chronicare.platform.medication.domain.queries.GetLogsByMedicationIdQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// ==================== Controller ====================
@RestController
@RequestMapping("/api/v1/medication-logs")
public class MedicationLogController {

    private final MedicationLogService logService;

    public MedicationLogController(MedicationLogService logService) {
        this.logService = logService;
    }

    // ==================== GET All Logs ====================
    @GetMapping
    public ResponseEntity<List<MedicationLogResponse>> getAllLogs() {
        List<MedicationLog> logs = logService.handle(new GetLogsByMedicationIdQuery(null));
        List<MedicationLogResponse> response = logs.stream()
                .map(MedicationLogResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // ==================== GET Log By ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<MedicationLogResponse> getLogById(@PathVariable Long id) {
        Optional<MedicationLog> log = logService.handle(new GetLogByIdQuery(id));
        return log.map(l -> ResponseEntity.ok(MedicationLogResponse.fromEntity(l)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ==================== GET Logs By Medication ID ====================
    @GetMapping("/medication/{medicationId}")
    public ResponseEntity<List<MedicationLogResponse>> getLogsByMedication(@PathVariable Long medicationId) {
        List<MedicationLog> logs = logService.handle(new GetLogsByMedicationIdQuery(medicationId));
        List<MedicationLogResponse> response = logs.stream()
                .map(MedicationLogResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // ==================== CREATE Log ====================
    @PostMapping
    public ResponseEntity<MedicationLogResponse> createLog(@RequestBody CreateMedicationLogResource resource) {
        CreateMedicationLogCommand command = new CreateMedicationLogCommand(
                resource.medicationId(),
                resource.timestamp(),
                resource.action()
        );
        MedicationLog created = logService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(MedicationLogResponse.fromEntity(created));
    }

    // ==================== UPDATE Log ====================
    @PutMapping("/{id}")
    public ResponseEntity<MedicationLogResponse> updateLog(@PathVariable Long id,
            @RequestBody UpdateMedicationLogResource resource) {
        UpdateMedicationLogCommand command = new UpdateMedicationLogCommand(
                id,
                resource.medicationId(),
                resource.timestamp(),
                resource.action()
        );
        MedicationLog updated = logService.handle(command);
        return ResponseEntity.ok(MedicationLogResponse.fromEntity(updated));
    }

    // ==================== DELETE Log ====================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLog(@PathVariable Long id) {
        logService.delete(id);
    }
}

// ==================== DTOs ====================
record CreateMedicationLogResource(Long medicationId, java.time.LocalDateTime timestamp, String action) {

}

record UpdateMedicationLogResource(Long medicationId, java.time.LocalDateTime timestamp, String action) {

}

record MedicationLogResponse(Long id, Long medicationId, java.time.LocalDateTime timestamp, String action) {

    public static MedicationLogResponse fromEntity(MedicationLog log) {
        return new MedicationLogResponse(
                log.getId(),
                log.getMedication() != null ? log.getMedication().getId() : null,
                log.getTimestamp(),
                log.getAction()
        );
    }
}
