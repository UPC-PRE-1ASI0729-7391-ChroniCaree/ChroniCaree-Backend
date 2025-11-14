/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patients.interfaces.rest;

import com.example.patients.application.services.PatientService;
import com.example.patients.domain.model.Patient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Patients", description = "Patient management API")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "List all patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> oPatient = patientService.getPatientById(id);
        if (oPatient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(oPatient.get());
    }

    @GetMapping("/dni/{dni}")
    @Operation(summary = "Get patient by DNI")
    public ResponseEntity<Patient> getPatientByDni(@PathVariable String dni) {
        Optional<Patient> oPatient = patientService.getPatientByDni(dni);
        if (oPatient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
         return ResponseEntity.ok(oPatient.get());
    }

    @PostMapping
    @Operation(summary = "Create new patient")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient created = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient by ID")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        Optional<Patient> oPatient = patientService.getPatientById(id);
        if (oPatient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patientService.updatePatient(id, patient));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient by ID")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        Optional<Patient> oPatient = patientService.getPatientById(id);
        if (oPatient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
