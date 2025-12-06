package com.chronicare.platform.appointments.application.internal.queryservices;

import com.chronicare.platform.appointments.domain.model.aggregates.Appointment;
import com.chronicare.platform.appointments.domain.model.queries.*;
import com.chronicare.platform.appointments.domain.services.AppointmentQueryService;
import com.chronicare.platform.appointments.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Summary: Appointment Query Service Implementation
 */
@Service
public class AppointmentQueryServiceImpl implements AppointmentQueryService {
    
    private final AppointmentRepository appointmentRepository;
    
    public AppointmentQueryServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Appointment> handle(GetAppointmentByIdQuery query) {
        return appointmentRepository.findById(query.appointmentId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Appointment> handle(GetAppointmentsByPatientQuery query) {
        return appointmentRepository.findByPatientId(query.patientId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Appointment> handle(GetAppointmentsByDoctorAndDateQuery query) {
        return appointmentRepository.findByDoctorIdAndDate(
            query.doctorId(), 
            query.date().toString()
        );
    }
}
