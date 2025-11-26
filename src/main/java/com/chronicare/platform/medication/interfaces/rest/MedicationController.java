/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.interfaces.rest;

import com.chronicare.platform.medication.application.services.MedicationService;
import com.chronicare.platform.medication.domain.aggregate.Medication;
import com.chronicare.platform.medication.domain.command.UpdateMedicationCommand;
import com.chronicare.platform.medication.domain.queries.GetAllMedicationsQuery;
import com.chronicare.platform.medication.domain.queries.GetMedicationByIdQuery;
import com.chronicare.platform.medication.domain.queries.GetMedicationsByPatientIdQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/medications")
@Tag(name = "Medications", description = "Medication management API (DDD version)")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    // ============================================
    // GET ALL
    // ============================================
    @GetMapping
    @Operation(summary = "List all medications")
    public ResponseEntity<List<Medication>> getAllMedications() {
        List<Medication> result = medicationService.handle(new GetAllMedicationsQuery());
        return ResponseEntity.ok(result);
    }

    // ============================================
    // GET BY ID
    // ============================================
    @GetMapping("/{id}")
    @Operation(summary = "Get medication by ID  ")
    public ResponseEntity<Medication> getMedicationById(@PathVariable Long id) {
        Optional<Medication> medication
                = medicationService.handle(new GetMedicationByIdQuery(id));

        return medication.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ============================================
    // GET BY PATIENT ID
    // ============================================
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get medications by patient ID ")
    public ResponseEntity<List<Medication>> getMedicationsByPatient(@PathVariable Long patientId) {
        List<Medication> result
                = medicationService.handle(new GetMedicationsByPatientIdQuery(patientId));

        return ResponseEntity.ok(result);
    }

    // ============================================
    // CREATE
    // ============================================
    @PostMapping
    @Operation(summary = "Create a new medication ")
    public ResponseEntity<Medication> createMedication(@RequestBody CreateMedicationCommand command) {
        Medication created = medicationService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ============================================
    // UPDATE
    // ============================================
    @PutMapping("/{id}")
    @Operation(summary = "Update medication by ID  ")
    public ResponseEntity<Medication> updateMedication(
            @PathVariable Long id,
            @RequestBody UpdateMedicationCommand command) {

        // Validar ID en path = ID en body
        if (!id.equals(command.id())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        Optional<Medication> found
                = medicationService.handle(new GetMedicationByIdQuery(id));

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Medication updated = medicationService.handle(command);
        return ResponseEntity.ok(updated);
    }

    // ============================================
    // DELETE
    // ============================================
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete medication by ID  ")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        Optional<Medication> found
                = medicationService.handle(new GetMedicationByIdQuery(id));

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        medicationService.handleDeleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}
