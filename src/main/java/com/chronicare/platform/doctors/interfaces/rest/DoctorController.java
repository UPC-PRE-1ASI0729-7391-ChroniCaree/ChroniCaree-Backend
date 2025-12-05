package com.chronicare.platform.doctors.interfaces.rest;

import com.chronicare.platform.doctors.domain.model.commands.*;
import com.chronicare.platform.doctors.domain.model.queries.*;
import com.chronicare.platform.doctors.domain.services.DoctorCommandService;
import com.chronicare.platform.doctors.domain.services.DoctorQueryService;
import com.chronicare.platform.doctors.interfaces.rest.resources.*;
import com.chronicare.platform.doctors.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/doctors", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Doctors", description = "Available Doctor Endpoints")
public class DoctorController {

    private final DoctorCommandService doctorCommandService;
    private final DoctorQueryService doctorQueryService;

    public DoctorController(DoctorCommandService doctorCommandService, DoctorQueryService doctorQueryService) {
        this.doctorCommandService = doctorCommandService;
        this.doctorQueryService = doctorQueryService;
    }

    // GET /api/v1/doctors - Get all doctors
    @GetMapping
    @Operation(summary = "Get all doctors")
    public ResponseEntity<List<DoctorResource>> getAllDoctors() {
        var getAllDoctorsQuery = new GetAllDoctorsQuery();
        var doctors = doctorQueryService.handle(getAllDoctorsQuery);
        var doctorResources = doctors.stream()
            .map(DoctorResourceFromEntityAssembler::toResourceFromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(doctorResources);
    }

    // GET /api/v1/doctors/{id} - Get doctor by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID")
    public ResponseEntity<DoctorResource> getDoctorById(@PathVariable Long id) {
        var getDoctorByIdQuery = new GetDoctorByIdQuery(id);
        var doctor = doctorQueryService.handle(getDoctorByIdQuery);
        if (doctor.isEmpty()) return ResponseEntity.notFound().build();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return ResponseEntity.ok(doctorResource);
    }

    // GET /api/v1/doctors/by-user/{userId} - Get doctor by user ID
    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Get doctor by user ID")
    public ResponseEntity<DoctorResource> getDoctorByUserId(@PathVariable Long userId) {
        var getDoctorByUserIdQuery = new GetDoctorByUserIdQuery(userId);
        var doctor = doctorQueryService.handle(getDoctorByUserIdQuery);
        if (doctor.isEmpty()) return ResponseEntity.notFound().build();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return ResponseEntity.ok(doctorResource);
    }

    // GET /api/v1/doctors/by-tenant/{tenantId} - Get doctors by tenant
    @GetMapping("/by-tenant/{tenantId}")
    @Operation(summary = "Get doctors by tenant")
    public ResponseEntity<List<DoctorResource>> getDoctorsByTenant(@PathVariable Long tenantId) {
        var getDoctorsByTenantQuery = new GetDoctorsByTenantQuery(tenantId);
        var doctors = doctorQueryService.handle(getDoctorsByTenantQuery);
        var doctorResources = doctors.stream()
            .map(DoctorResourceFromEntityAssembler::toResourceFromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(doctorResources);
    }

    // GET /api/v1/doctors/by-specialty/{specialty} - Get doctors by specialty
    @GetMapping("/by-specialty/{specialty}")
    @Operation(summary = "Get doctors by specialty")
    public ResponseEntity<List<DoctorResource>> getDoctorsBySpecialty(@PathVariable String specialty) {
        var getDoctorsBySpecialtyQuery = new GetDoctorsBySpecialtyQuery(specialty);
        var doctors = doctorQueryService.handle(getDoctorsBySpecialtyQuery);
        var doctorResources = doctors.stream()
            .map(DoctorResourceFromEntityAssembler::toResourceFromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(doctorResources);
    }

    // POST /api/v1/doctors - Create doctor
    @PostMapping
    @Operation(summary = "Create doctor")
    public ResponseEntity<DoctorResource> createDoctor(@RequestBody CreateDoctorResource resource) {
        var createDoctorCommand = CreateDoctorCommandFromResourceAssembler.toCommandFromResource(resource);
        var doctor = doctorCommandService.handle(createDoctorCommand);
        if (doctor.isEmpty()) return ResponseEntity.badRequest().build();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return new ResponseEntity<>(doctorResource, HttpStatus.CREATED);
    }

    // PUT /api/v1/doctors/{id} - Update doctor
    @PutMapping("/{id}")
    @Operation(summary = "Update doctor")
    public ResponseEntity<DoctorResource> updateDoctor(@PathVariable Long id, @RequestBody UpdateDoctorResource resource) {
        var updateDoctorCommand = UpdateDoctorCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var doctor = doctorCommandService.handle(updateDoctorCommand);
        if (doctor.isEmpty()) return ResponseEntity.notFound().build();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return ResponseEntity.ok(doctorResource);
    }

    // PUT /api/v1/doctors/{id}/verify - Verify doctor
    @PutMapping("/{id}/verify")
    @Operation(summary = "Verify doctor")
    public ResponseEntity<DoctorResource> verifyDoctor(@PathVariable Long id) {
        var verifyDoctorCommand = new VerifyDoctorCommand(id);
        var doctor = doctorCommandService.handle(verifyDoctorCommand);
        if (doctor.isEmpty()) return ResponseEntity.notFound().build();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return ResponseEntity.ok(doctorResource);
    }

    // PUT /api/v1/doctors/{id}/accepting-patients - Update accepting patients
    @PutMapping("/{id}/accepting-patients")
    @Operation(summary = "Update accepting patients status")
    public ResponseEntity<DoctorResource> updateAcceptingPatients(
        @PathVariable Long id, 
        @RequestBody UpdateAcceptingPatientsResource resource
    ) {
        var updateAcceptingPatientsCommand = new UpdateAcceptingPatientsCommand(id, resource.acceptingPatients());
        var doctor = doctorCommandService.handle(updateAcceptingPatientsCommand);
        if (doctor.isEmpty()) return ResponseEntity.notFound().build();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return ResponseEntity.ok(doctorResource);
    }

    // GET /api/v1/doctors/{id}/assigned-patients - Get assigned patients
    @GetMapping("/{id}/assigned-patients")
    @Operation(summary = "Get doctor's assigned patients")
    public ResponseEntity<List<?>> getAssignedPatients(@PathVariable Long id) {
        var getAssignedPatientsQuery = new GetAssignedPatientsQuery(id);
        var patients = doctorQueryService.handle(getAssignedPatientsQuery);
        return ResponseEntity.ok(patients);
    }

    // DELETE /api/v1/doctors/{id} - Delete doctor
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        var deleteDoctorCommand = new DeleteDoctorCommand(id);
        doctorCommandService.handle(deleteDoctorCommand);
        return ResponseEntity.noContent().build();
    }
}
