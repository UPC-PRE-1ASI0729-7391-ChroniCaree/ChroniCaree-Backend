package com.example.doctors.interfaces.rest;

import com.example.doctors.application.services.DoctorService;
import com.example.doctors.domain.model.Doctor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de médicos.
 */
@RestController
@RequestMapping("/api/v1/doctors")
@Tag(name = "Doctors", description = "Doctor management API")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    @Operation(summary = "List all doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> oDoctor = doctorService.getDoctorById(id);
        if (oDoctor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(oDoctor.get());
    }

    @PostMapping
    @Operation(summary = "Create new doctor")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor created = doctorService.createDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update doctor by ID")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        Optional<Doctor> oDoctor = doctorService.getDoctorById(id);
        if (oDoctor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctor));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor by ID")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        Optional<Doctor> oDoctor = doctorService.getDoctorById(id);
        if (oDoctor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
