package com.chronicare.platform.medication.interfaces.rest.transform;

import com.chronicare.platform.medication.domain.model.aggregates.Medication;
import com.chronicare.platform.medication.interfaces.rest.resources.MedicationResource;
import com.chronicare.platform.medication.interfaces.rest.resources.MedicationScheduleResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Assembler to convert Medication entity to MedicationResource
 * Creates nested schedule object matching frontend expectations
 */
public class MedicationResourceFromEntityAssembler {
    
    private MedicationResourceFromEntityAssembler() {
        // Private constructor to prevent instantiation
    }

    public static MedicationResource toResourceFromEntity(Medication entity) {
        // Null check para evitar NullPointerException
        if (entity == null) {
            throw new IllegalArgumentException("Medication entity cannot be null");
        }
        
        // Validar que los campos críticos no sean null
        if (entity.getSchedule() == null) {
            throw new IllegalStateException("Medication schedule cannot be null for medication ID: " + entity.getId());
        }
        
        if (entity.getSchedule().getFrequency() == null) {
            throw new IllegalStateException("Medication frequency cannot be null for medication ID: " + entity.getId());
        }
        
        // Crear el objeto schedule anidado
        MedicationScheduleResource schedule = createScheduleResource(entity);
        
        // Convertir patientId de String a Long si es necesario
        Long patientId;
        try {
            patientId = Long.parseLong(entity.getPatientId());
        } catch (NumberFormatException _) {
            throw new IllegalStateException("Invalid patientId format for medication ID: " + entity.getId());
        }
        
        return new MedicationResource(
                entity.getId(),
                patientId,
                entity.getName(),
                entity.getType(),
                entity.getDosage(),
                schedule,  // Objeto schedule anidado con frequency, times, startDate, endDate
                entity.getPrescribedBy(),
                entity.getPrescribedDate(),
                entity.getStatus(),
                entity.getInstructions(),
                entity.getSideEffects(),
                entity.getContraindications(),
                entity.getPurpose(),
                entity.getRefillDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
    
    /**
     * Creates MedicationScheduleResource from Medication entity
     * Converts timeOfDay string "09:00,21:00" to array ["09:00", "21:00"]
     */
    private static MedicationScheduleResource createScheduleResource(Medication entity) {
        // Convertir frequency enum a string
        String frequency = entity.getSchedule().getFrequency().name();
        
        // Convertir timeOfDay string "09:00,21:00" a List<String> ["09:00", "21:00"]
        List<String> times = new ArrayList<>();
        if (entity.getSchedule().getTimeOfDay() != null && !entity.getSchedule().getTimeOfDay().isEmpty()) {
            String[] timesArray = entity.getSchedule().getTimeOfDay().split(",");
            times = Arrays.asList(timesArray);
        }
        
        // startDate es el prescribedDate
        String startDate = entity.getPrescribedDate() != null ? 
            entity.getPrescribedDate().toString() : null;
        
        // endDate es el refillDate (opcional)
        String endDate = entity.getRefillDate() != null ? 
            entity.getRefillDate().toString() : null;
        
        return new MedicationScheduleResource(frequency, times, startDate, endDate);
    }
}
