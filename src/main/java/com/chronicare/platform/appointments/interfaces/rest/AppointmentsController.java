package com.chronicare.platform.appointments.interfaces.rest;

import com.chronicare.platform.appointments.domain.model.commands.*;
import com.chronicare.platform.appointments.domain.model.queries.*;
import com.chronicare.platform.appointments.domain.services.AppointmentCommandService;
import com.chronicare.platform.appointments.domain.services.AppointmentQueryService;
import com.chronicare.platform.appointments.interfaces.rest.resources.*;
import com.chronicare.platform.appointments.interfaces.rest.transform.AppointmentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Summary: Appointments Controller
 * Exposes endpoints for appointment management
 */
@RestController
@RequestMapping("/api/v1/appointments")
@Tag(name = "Appointments", description = "Appointment Management Endpoints")
public class AppointmentsController {
    
    private final AppointmentCommandService appointmentCommandService;
    private final AppointmentQueryService appointmentQueryService;
    
    public AppointmentsController(
        AppointmentCommandService appointmentCommandService,
        AppointmentQueryService appointmentQueryService
    ) {
        this.appointmentCommandService = appointmentCommandService;
        this.appointmentQueryService = appointmentQueryService;
    }
    
    /**
     * Create a new appointment
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'HOSPITAL_ADMIN', 'PATIENT')")
    public ResponseEntity<AppointmentResource> createAppointment(@RequestBody CreateAppointmentResource resource) {
        var command = new CreateAppointmentCommand(
            resource.patientId(),
            resource.doctorId(),
            resource.appointmentDate(),
            resource.appointmentTime(),
            resource.appointmentType(),
            resource.reason(),
            resource.location(),
            resource.notes(),
            resource.duration()
        );
        
        var appointment = appointmentCommandService.handle(command);
        
        if (appointment.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return new ResponseEntity<>(appointmentResource, HttpStatus.CREATED);
    }
    
    /**
     * Get appointment by ID
     */
    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'HOSPITAL_ADMIN', 'PATIENT')")
    public ResponseEntity<AppointmentResource> getAppointment(@PathVariable Long appointmentId) {
        var query = new GetAppointmentByIdQuery(appointmentId);
        var appointment = appointmentQueryService.handle(query);
        
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(resource);
    }
    
    /**
     * Get appointments by patient
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'HOSPITAL_ADMIN', 'PATIENT')")
    public ResponseEntity<List<AppointmentResource>> getAppointmentsByPatient(@PathVariable Long patientId) {
        var query = new GetAppointmentsByPatientQuery(patientId);
        var appointments = appointmentQueryService.handle(query);
        
        var resources = appointments.stream()
            .map(AppointmentResourceFromEntityAssembler::toResourceFromEntity)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(resources);
    }
    
    /**
     * Get appointments by doctor and date
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'HOSPITAL_ADMIN')")
    public ResponseEntity<List<AppointmentResource>> getAppointmentsByDoctorAndDate(
        @PathVariable Long doctorId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        var query = new GetAppointmentsByDoctorAndDateQuery(doctorId, date);
        var appointments = appointmentQueryService.handle(query);
        
        var resources = appointments.stream()
            .map(AppointmentResourceFromEntityAssembler::toResourceFromEntity)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(resources);
    }
    
    /**
     * Update appointment
     */
    @PutMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'HOSPITAL_ADMIN')")
    public ResponseEntity<AppointmentResource> updateAppointment(
        @PathVariable Long appointmentId,
        @RequestBody UpdateAppointmentResource resource
    ) {
        var command = new UpdateAppointmentCommand(
            appointmentId,
            resource.appointmentDate(),
            resource.appointmentTime(),
            resource.appointmentType(),
            resource.reason(),
            resource.location(),
            resource.notes(),
            resource.duration()
        );
        
        var appointment = appointmentCommandService.handle(command);
        
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }
    
    /**
     * Confirm appointment
     */
    @PostMapping("/{appointmentId}/confirm")
    @PreAuthorize("hasAnyRole('DOCTOR', 'HOSPITAL_ADMIN')")
    public ResponseEntity<AppointmentResource> confirmAppointment(@PathVariable Long appointmentId) {
        var command = new ConfirmAppointmentCommand(appointmentId);
        var appointment = appointmentCommandService.handle(command);
        
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(resource);
    }
    
    /**
     * Complete appointment
     */
    @PostMapping("/{appointmentId}/complete")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentResource> completeAppointment(
        @PathVariable Long appointmentId,
        @RequestBody CompleteAppointmentResource resource
    ) {
        var command = new CompleteAppointmentCommand(appointmentId, resource.completionNotes());
        var appointment = appointmentCommandService.handle(command);
        
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }
    
    /**
     * Cancel appointment
     */
    @PostMapping("/{appointmentId}/cancel")
    @PreAuthorize("hasAnyRole('DOCTOR', 'HOSPITAL_ADMIN', 'PATIENT')")
    public ResponseEntity<AppointmentResource> cancelAppointment(
        @PathVariable Long appointmentId,
        @RequestBody CancelAppointmentResource resource
    ) {
        var command = new CancelAppointmentCommand(appointmentId, resource.cancellationReason());
        var appointment = appointmentCommandService.handle(command);
        
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }
}
