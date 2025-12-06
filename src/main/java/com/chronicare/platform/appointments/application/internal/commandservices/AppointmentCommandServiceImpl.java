package com.chronicare.platform.appointments.application.internal.commandservices;

import com.chronicare.platform.appointments.domain.model.AppointmentStatus;
import com.chronicare.platform.appointments.domain.model.AppointmentType;
import com.chronicare.platform.appointments.domain.model.aggregates.Appointment;
import com.chronicare.platform.appointments.domain.model.commands.*;
import com.chronicare.platform.appointments.domain.services.AppointmentCommandService;
import com.chronicare.platform.appointments.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Summary: Appointment Command Service Implementation
 */
@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {
    
    private final AppointmentRepository appointmentRepository;
    private final JdbcTemplate jdbcTemplate;
    
    public AppointmentCommandServiceImpl(AppointmentRepository appointmentRepository, JdbcTemplate jdbcTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    @Transactional
    public Optional<Appointment> handle(CreateAppointmentCommand command) {
        // Validate conflict
        if (hasConflict(command.doctorId(), command.appointmentDate(), command.appointmentTime(), command.duration())) {
            throw new IllegalStateException("Appointment time conflicts with existing appointment");
        }
        
        // Create appointment
        Appointment appointment = Appointment.builder()
            .patientId(command.patientId())
            .doctorId(command.doctorId())
            .date(command.appointmentDate().toString())
            .time(command.appointmentTime().toString())
            .type(AppointmentType.valueOf(command.appointmentType()))
            .status(AppointmentStatus.SCHEDULED)
            .notes(command.notes())
            .duration(command.duration() != null ? command.duration() : 30)
            .build();
        
        Appointment saved = appointmentRepository.save(appointment);
        return Optional.of(saved);
    }
    
    @Override
    @Transactional
    public Optional<Appointment> handle(UpdateAppointmentCommand command) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(command.appointmentId());
        if (appointmentOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Appointment appointment = appointmentOpt.get();
        
        // Check if can update (only SCHEDULED or CONFIRMED can be updated)
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED && 
            appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot update appointment with status: " + appointment.getStatus());
        }
        
        // Update fields if provided
        if (command.appointmentDate() != null) {
            // Validate conflict with new date/time
            LocalDate newDate = command.appointmentDate();
            LocalTime newTime = command.appointmentTime() != null 
                ? command.appointmentTime() 
                : LocalTime.parse(appointment.getTime());
            Integer duration = command.duration() != null ? command.duration() : appointment.getDuration();
            
            if (hasConflictExcluding(appointment.getDoctorId(), newDate, newTime, duration, appointment.getId())) {
                throw new IllegalStateException("Updated time conflicts with existing appointment");
            }
            
            appointment.setDate(newDate.toString());
        }
        
        if (command.appointmentTime() != null) {
            appointment.setTime(command.appointmentTime().toString());
        }
        
        if (command.appointmentType() != null) {
            appointment.setType(AppointmentType.valueOf(command.appointmentType()));
        }
        
        if (command.notes() != null) {
            appointment.setNotes(command.notes());
        }
        
        if (command.duration() != null) {
            appointment.setDuration(command.duration());
        }
        
        Appointment updated = appointmentRepository.save(appointment);
        return Optional.of(updated);
    }
    
    @Override
    @Transactional
    public Optional<Appointment> handle(ConfirmAppointmentCommand command) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(command.appointmentId());
        if (appointmentOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Appointment appointment = appointmentOpt.get();
        
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Can only confirm SCHEDULED appointments");
        }
        
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment updated = appointmentRepository.save(appointment);
        return Optional.of(updated);
    }
    
    @Override
    @Transactional
    public Optional<Appointment> handle(CompleteAppointmentCommand command) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(command.appointmentId());
        if (appointmentOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Appointment appointment = appointmentOpt.get();
        
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot complete cancelled appointment");
        }
        
        appointment.setStatus(AppointmentStatus.COMPLETED);
        if (command.completionNotes() != null) {
            String existingNotes = appointment.getNotes() != null ? appointment.getNotes() + "\n" : "";
            appointment.setNotes(existingNotes + "Completion notes: " + command.completionNotes());
        }
        
        Appointment updated = appointmentRepository.save(appointment);
        return Optional.of(updated);
    }
    
    @Override
    @Transactional
    public Optional<Appointment> handle(CancelAppointmentCommand command) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(command.appointmentId());
        if (appointmentOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Appointment appointment = appointmentOpt.get();
        
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed appointment");
        }
        
        appointment.setStatus(AppointmentStatus.CANCELLED);
        if (command.cancellationReason() != null) {
            String existingNotes = appointment.getNotes() != null ? appointment.getNotes() + "\n" : "";
            appointment.setNotes(existingNotes + "Cancellation reason: " + command.cancellationReason());
        }
        
        Appointment updated = appointmentRepository.save(appointment);
        return Optional.of(updated);
    }
    
    private boolean hasConflict(Long doctorId, LocalDate date, LocalTime time, Integer duration) {
        return hasConflictExcluding(doctorId, date, time, duration, null);
    }
    
    private boolean hasConflictExcluding(Long doctorId, LocalDate date, LocalTime time, Integer duration, Long excludeId) {
        int durationMinutes = duration != null ? duration : 30;
        
        // Calculate end time
        LocalTime endTime = time.plusMinutes(durationMinutes);
        
        // Query for conflicts
        String sql = """
            SELECT COUNT(*) FROM appointments
            WHERE doctor_id = ?
              AND date = ?
              AND status IN ('SCHEDULED', 'CONFIRMED')
              AND (? IS NULL OR id != ?)
              AND (
                (TIME(time) < ? AND ADDTIME(TIME(time), SEC_TO_TIME(duration * 60)) > ?)
                OR (TIME(time) >= ? AND TIME(time) < ?)
              )
        """;
        
        Integer conflicts = jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            doctorId,
            date.toString(),
            excludeId,
            excludeId,
            endTime.toString(),
            time.toString(),
            time.toString(),
            endTime.toString()
        );
        
        return conflicts != null && conflicts > 0;
    }
}
