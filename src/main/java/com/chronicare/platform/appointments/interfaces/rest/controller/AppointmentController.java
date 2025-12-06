package com.chronicare.platform.appointments.interfaces.rest.controller;

import com.chronicare.platform.appointments.application.service.AppointmentService;
import com.chronicare.platform.appointments.domain.model.aggregates.Appointment;
import com.chronicare.platform.appointments.domain.model.valueobjects.AppointmentType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<AppointmentDTO> createAppointment(
            @RequestBody CreateAppointmentRequest request,
            @RequestHeader(name = "X-Tenant-ID") String tenantId,
            @RequestHeader(name = "X-User-ID") String userId) {

        UUID appointmentId = appointmentService.createAppointment(
            tenantId,
            request.patientId,
            request.doctorId,
            request.startAt,
            request.endAt,
            request.durationMinutes,
            AppointmentType.valueOf(request.type),
            userId
        );

        Optional<Appointment> appointment = appointmentService.getAppointment(appointmentId);
        if (appointment.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(appointment.get()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<AppointmentDTO> getAppointment(
            @PathVariable UUID appointmentId) {

        Optional<Appointment> appointment = appointmentService.getAppointment(appointmentId);
        return appointment.map(app -> ResponseEntity.ok(toDTO(app)))
                         .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<List<AppointmentDTO>> listAppointments(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String doctorId,
            @RequestHeader(name = "X-Tenant-ID") String tenantId) {

        List<Appointment> appointments;
        if (patientId != null) {
            appointments = appointmentService.getAppointmentsByPatient(patientId, tenantId);
        } else if (doctorId != null) {
            appointments = appointmentService.getAppointmentsByDoctor(doctorId, tenantId);
        } else {
            return ResponseEntity.badRequest().build();
        }

        List<AppointmentDTO> dtos = appointments.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{appointmentId}/confirm")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> confirmAppointment(
            @PathVariable UUID appointmentId,
            @RequestHeader(name = "X-Tenant-ID") String tenantId) {

        try {
            appointmentService.confirmAppointment(appointmentId, tenantId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{appointmentId}/complete")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> completeAppointment(
            @PathVariable UUID appointmentId,
            @RequestHeader(name = "X-Tenant-ID") String tenantId) {

        try {
            appointmentService.completeAppointment(appointmentId, tenantId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{appointmentId}/cancel")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable UUID appointmentId,
            @RequestBody CancelRequest request,
            @RequestHeader(name = "X-Tenant-ID") String tenantId) {

        try {
            appointmentService.cancelAppointment(appointmentId, tenantId, request.reason);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private AppointmentDTO toDTO(Appointment appointment) {
        return new AppointmentDTO(
            appointment.getAppointmentId(),
            appointment.getTenantId(),
            appointment.getPatientId(),
            appointment.getDoctorId(),
            appointment.getStatus().getCode(),
            appointment.getStartAt(),
            appointment.getEndAt(),
            appointment.getDurationMinutes(),
            appointment.getType().getCode(),
            appointment.getNotes()
        );
    }

    public record CreateAppointmentRequest(
        String patientId,
        String doctorId,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Integer durationMinutes,
        String type
    ) {}

    public record CancelRequest(String reason) {}

    public record AppointmentDTO(
        UUID appointmentId,
        String tenantId,
        String patientId,
        String doctorId,
        String status,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Integer durationMinutes,
        String type,
        String notes
    ) {}
}
