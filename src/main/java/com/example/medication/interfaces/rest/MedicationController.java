/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.medication.interfaces.rest;

import com.example.medication.application.services.MedicationService;
import com.example.medication.domain.model.Medication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medications")
@Tag(name = "Medications", description = "Medication management API")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    @Operation(summary = "List all medications")
    public ResponseEntity<List<Medication>> getAllMedications() {
        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get medication by ID")
    public ResponseEntity<Medication> getMedicationById(@PathVariable String id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get medications by patient ID")
    public ResponseEntity<List<Medication>> getMedicationsByPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(medicationService.getMedicationsByPatient(patientId));
    }

    @PostMapping
    @Operation(summary = "Create a new medication")
    public ResponseEntity<Medication> createMedication(@RequestBody Medication medication) {
        Medication created = medicationService.createMedication(medication);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update medication by ID")
    public ResponseEntity<Medication> updateMedication(@PathVariable String id, @RequestBody Medication medication) {
        return ResponseEntity.ok(medicationService.updateMedication(id, medication));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete medication by ID")
    public ResponseEntity<Void> deleteMedication(@PathVariable String id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}
