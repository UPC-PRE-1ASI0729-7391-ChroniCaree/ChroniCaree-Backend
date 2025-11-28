package com.chronicare.platform.doctors.interfaces.rest;

import com.chronicare.platform.doctors.application.services.DoctorService;
import com.chronicare.platform.doctors.domain.model.aggregates.Doctor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * DoctorController
 * <p>
 *     This controller is responsible for handling all the requests related to doctors.
 *     It exposes the following endpoints:
 *     <ul>
 *         <li>GET /api/v1/doctors: Get all doctors</li>
 *         <li>GET /api/v1/doctors/{id}: Get doctor by ID</li>
 *         <li>POST /api/v1/doctors: Create new doctor</li>
 *         <li>PUT /api/v1/doctors/{id}: Update doctor</li>
 *         <li>DELETE /api/v1/doctors/{id}: Delete doctor</li>
 *     </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/doctors", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Doctors", description = "Available Doctor Endpoints")
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * Constructor
     * @param doctorService The {@link DoctorService} instance
     */
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * Get all doctors
     * @return A list of all doctors in the system
     */
    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieve all registered doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    /**
     * Get doctor by ID
     * @param id The doctor ID
     * @return The doctor if found, or a 404 response if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID", description = "Retrieve a specific doctor by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> oDoctor = doctorService.getDoctorById(id);
        if (oDoctor.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(oDoctor.get());
    }

    /**
     * Create a new doctor
     * @param doctor The doctor data
     * @return The created doctor with 201 status
     */
    @PostMapping
    @Operation(summary = "Create new doctor", description = "Register a new doctor in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor created = doctorService.createDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update doctor by ID
     * @param id The doctor ID
     * @param doctor The updated doctor data
     * @return The updated doctor, or a 404 response if not found
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update doctor", description = "Update doctor information by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        Optional<Doctor> oDoctor = doctorService.getDoctorById(id);
        if (oDoctor.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctor));
    }

    /**
     * Delete doctor by ID
     * @param id The doctor ID
     * @return A 204 No Content response if deleted successfully, or 404 if not found
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor", description = "Delete a doctor from the system by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        Optional<Doctor> oDoctor = doctorService.getDoctorById(id);
        if (oDoctor.isEmpty()) return ResponseEntity.notFound().build();
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
