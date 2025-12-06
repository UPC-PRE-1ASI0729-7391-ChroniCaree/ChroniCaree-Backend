package com.chronicare.platform.medicalRecords.application.internal.commandservices;

import com.chronicare.platform.medicalRecords.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.medicalRecords.domain.model.commands.CreateMedicalRecordCommand;
import com.chronicare.platform.medicalRecords.domain.model.commands.DeleteMedicalRecordCommand;
import com.chronicare.platform.medicalRecords.domain.model.commands.UpdateMedicalRecordCommand;
import com.chronicare.platform.medicalRecords.domain.repository.MedicalRecordRepository;
import com.chronicare.platform.medicalRecords.domain.services.MedicalRecordCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * MedicalRecordCommandService Implementation
 *
 * @summary
 * Handles Medical Record commands and applies domain business rules.
 * Business rules enforced:
 * - Creation of medical records delegates validation to the MedicalRecord aggregate.
 * - Updates are only applied to existing medical records.
 * - Deletion is restricted to records that exist in the system.
 *
 */

@Service
public class MedicalRecordCommandServiceImpl implements MedicalRecordCommandService {
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordCommandServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    @Transactional
    public MedicalRecord handle(CreateMedicalRecordCommand command) {
        var medicalRecord = new MedicalRecord(command);
        return medicalRecordRepository.save(medicalRecord);
    }

    @Override
    @Transactional
    public MedicalRecord handle(UpdateMedicalRecordCommand command) {
        var medicalRecord = medicalRecordRepository.findById(command.medicalRecordId())
                .orElseThrow(() -> new IllegalArgumentException("Medical record not found"));
        medicalRecord.update(command);
        return medicalRecordRepository.save(medicalRecord);
    }

    @Override
    @Transactional
    public void handle(DeleteMedicalRecordCommand command) {
        if (!medicalRecordRepository.existsById(command.medicalRecordId())) {
            throw new IllegalArgumentException("Medical record not found");
        }
        medicalRecordRepository.deleteById(command.medicalRecordId());
    }
}

