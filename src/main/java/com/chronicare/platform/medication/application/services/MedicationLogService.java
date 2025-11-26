/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.application.services;

import com.chronicare.platform.medication.domain.aggregate.Medication;
import com.chronicare.platform.medication.domain.aggregate.MedicationLog;
import com.chronicare.platform.medication.domain.command.CreateMedicationLogCommand;
import com.chronicare.platform.medication.domain.command.UpdateMedicationLogCommand;
import com.chronicare.platform.medication.domain.event.MedicationLogCreatedEvent;
import com.chronicare.platform.medication.domain.event.MedicationLogUpdatedEvent;
import com.chronicare.platform.medication.domain.queries.GetLogByIdQuery;
import com.chronicare.platform.medication.domain.queries.GetLogsByMedicationIdQuery;
import com.chronicare.platform.medication.domain.repository.MedicationLogRepository;
import com.chronicare.platform.medication.domain.repository.MedicationRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicationLogService {

    private final MedicationLogRepository logRepository;
    private final MedicationRepository medicationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MedicationLogService(MedicationLogRepository logRepository,
            MedicationRepository medicationRepository,
            ApplicationEventPublisher eventPublisher) {
        this.logRepository = logRepository;
        this.medicationRepository = medicationRepository;
        this.eventPublisher = eventPublisher;
    }

    // ----------------------------
    // Queries
    // ----------------------------
    public Optional<MedicationLog> handle(GetLogByIdQuery query) {
        return logRepository.findById(query.logId());
    }

    public List<MedicationLog> handle(GetLogsByMedicationIdQuery query) {
        return logRepository.findByMedicationId(query.medicationId());
    }

    // ----------------------------
    // Commands
    // ----------------------------
    public MedicationLog handle(CreateMedicationLogCommand command) {
        // Buscar la Medication existente
        Medication medication = medicationRepository.findById(command.medicationId())
                .orElseThrow(() -> new IllegalArgumentException("Medication not found"));

        // Crear el log
        MedicationLog log = new MedicationLog();
        log.setTimestamp(command.timestamp());
        log.setAction(command.action());
        log.setMedication(medication);

        MedicationLog saved = logRepository.save(log);

        // Publicar evento
        eventPublisher.publishEvent(new MedicationLogCreatedEvent(
                this,
                saved.getId(),
                saved.getMedication().getId(),
                saved.getTimestamp(),
                saved.getAction()
        ));

        return saved;
    }

    public MedicationLog handle(UpdateMedicationLogCommand command) {
        // Buscar log existente
        MedicationLog log = logRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("MedicationLog not found"));

        // Actualizar campos
        log.updateFrom(command);
        MedicationLog updated = logRepository.save(log);

        // Publicar evento
        eventPublisher.publishEvent(new MedicationLogUpdatedEvent(
                this,
                updated.getId(),
                updated.getMedication().getId(),
                updated.getTimestamp(),
                updated.getAction()
        ));

        return updated;
    }

    // ----------------------------
    // Delete
    // ----------------------------
    public void delete(Long id) {
        logRepository.deleteById(id);
    }
}

