package com.chronicare.platform.medication.application.internal.commandservices;

import com.chronicare.platform.medication.domain.model.aggregates.Medication;
import com.chronicare.platform.medication.domain.model.commands.CreateMedicationCommand;
import com.chronicare.platform.medication.domain.model.commands.DeleteMedicationCommand;
import com.chronicare.platform.medication.domain.model.commands.UpdateMedicationCommand;
import com.chronicare.platform.medication.domain.services.MedicationCommandService;
import com.chronicare.platform.medication.infrastructure.persistence.jpa.repositories.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * MedicationCommandService Implementation
 *
 * @summary
 * Handles Medication command operations and enforces domain business rules.
 * Business rules enforced:
 * - Creation of medications delegates validation to the Medication aggregate.
 * - Updates are only applied to existing medications.
 * - Deletion operates directly and assumes existence checks are handled externally.
 *
 */

@Service
@Transactional
public class MedicationCommandServiceImpl implements MedicationCommandService {

    private final MedicationRepository medicationRepository;

    public MedicationCommandServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public Optional<Medication> handle(CreateMedicationCommand command) {
        var medication = new Medication(command);
        var savedMedication = medicationRepository.save(medication);
        return Optional.of(savedMedication);
    }

    @Override
    public Optional<Medication> handle(UpdateMedicationCommand command) {
        var existingMedication = medicationRepository.findById(command.id());
        if (existingMedication.isEmpty()) {
            return Optional.empty();
        }

        var medication = existingMedication.get();
        medication.updateMedication(
                command.name(),
                command.type(),
                command.dosage(),
                command.frequency(),
                command.timeOfDay(),
                command.status(),
                command.instructions(),
                command.sideEffects(),
                command.contraindications(),
                command.purpose(),
                command.refillDate()
        );

        var updatedMedication = medicationRepository.save(medication);
        return Optional.of(updatedMedication);
    }

    @Override
    public void handle(DeleteMedicationCommand command) {
        medicationRepository.deleteById(command.id());
    }
}
