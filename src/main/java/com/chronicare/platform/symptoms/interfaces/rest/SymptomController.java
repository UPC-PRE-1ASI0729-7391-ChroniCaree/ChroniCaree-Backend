/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.symptoms.interfaces.rest;

import com.chronicare.platform.symptoms.application.services.SymptomService;
import com.chronicare.platform.symptoms.domain.model.aggregates.Symptom;
import com.chronicare.platform.symptoms.domain.commands.CreateSymptomCommand;
import com.chronicare.platform.symptoms.domain.commands.UpdateSymptomCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/symptoms")
public class SymptomController {

    private final SymptomService service;

    public SymptomController(SymptomService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Symptom>> getAllSymptoms() {
        return ResponseEntity.ok(service.getAllSymptoms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Symptom> getSymptomById(@PathVariable Long id) {
        return service.getSymptomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Symptom>> getSymptomsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.getSymptomsByPatientId(patientId));
    }

    @PostMapping
    public ResponseEntity<Symptom> createSymptom(@RequestBody CreateSymptomCommand command) {
        Symptom created = service.createSymptom(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Symptom> updateSymptom(@PathVariable Long id, @RequestBody UpdateSymptomCommand command) {
        return ResponseEntity.ok(service.updateSymptom(id, command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSymptom(@PathVariable Long id) {
        service.deleteSymptom(id);
        return ResponseEntity.noContent().build();
    }
}
