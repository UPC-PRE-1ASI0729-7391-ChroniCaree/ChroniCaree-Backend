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
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'HOSPITAL_ADMIN', 'SYSTEM', 'PATIENT', 'TENANT_ADMIN')")
        public ResponseEntity<List<Symptom>> getAllSymptoms(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        if (patientId != null && !patientId.isBlank()) {
            if (!patientId.matches("\\d+")) {
                return ResponseEntity.badRequest().build();
            }
            Long pid = Long.parseLong(patientId);
            var symptoms = service.getSymptomsByPatientId(pid);
            java.time.Instant f = null;
            java.time.Instant t = null;
            try {
                if (from != null && !from.isBlank()) f = java.time.Instant.parse(from);
                if (to != null && !to.isBlank()) t = java.time.Instant.parse(to);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
            if (f != null || t != null) {
                var finalF = f;
                var finalT = t;
                symptoms = symptoms.stream()
                        .filter(s -> {
                                    var ts = s.getTimestamp() != null ? s.getTimestamp().atZone(java.time.ZoneId.systemDefault()).toInstant() : null;
                            if (ts == null) return false;
                            if (finalF != null && ts.isBefore(finalF)) return false;
                            if (finalT != null && ts.isAfter(finalT)) return false;
                            return true;
                        }).toList();
            }
            int fromIndex = Math.max(0, page * limit);
            int toIndex = Math.min(symptoms.size(), fromIndex + limit);
            var pageContent = symptoms.subList(fromIndex, toIndex);
            return ResponseEntity.ok(pageContent);
        }
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
