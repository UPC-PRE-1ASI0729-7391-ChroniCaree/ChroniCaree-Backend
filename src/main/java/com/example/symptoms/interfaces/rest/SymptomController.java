/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.symptoms.interfaces.rest;

import com.example.symptoms.application.services.SymptomService;
import com.example.symptoms.domain.model.Symptom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/symptoms")
@Tag(name = "Symptoms", description = "Symptom management API")
public class SymptomController {

    private final SymptomService symptomService;

    public SymptomController(SymptomService symptomService) {
        this.symptomService = symptomService;
    }

    @GetMapping
    @Operation(summary = "List all symptoms")
    public ResponseEntity<List<Symptom>> getAllSymptoms() {
        return ResponseEntity.ok(symptomService.getAllSymptoms());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get symptom by ID")
    public ResponseEntity<Symptom> getSymptomById(@PathVariable Long id) {
        Optional<Symptom> oAppointment = symptomService.getSymptomById(id);
        if (oAppointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        symptomService.deleteSymptom(id);
        return ResponseEntity.ok(oAppointment.get());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get symptoms by patient ID")
    public ResponseEntity<List<Symptom>> getSymptomsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(symptomService.getSymptomsByPatientId(patientId));
    }

    @PostMapping
    @Operation(summary = "Create new symptom")
    public ResponseEntity<Symptom> createSymptom(@RequestBody Symptom symptom) {
        Symptom created = symptomService.createSymptom(symptom);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update symptom by ID")
    public ResponseEntity<Symptom> updateSymptom(@PathVariable Long id, @RequestBody Symptom symptom) {
        Optional<Symptom> oAppointment = symptomService.getSymptomById(id);
        if (oAppointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(symptomService.updateSymptom(id, symptom));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete symptom by ID")
    public ResponseEntity<Void> deleteSymptom(@PathVariable Long id) {
        Optional<Symptom> oAppointment = symptomService.getSymptomById(id);
        if (oAppointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        symptomService.deleteSymptom(id);
        return ResponseEntity.noContent().build();
    }
}
