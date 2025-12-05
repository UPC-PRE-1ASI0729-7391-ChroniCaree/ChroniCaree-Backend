package com.chronicare.platform.patients.domain.services;

import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.commands.CreatePatientCommand;
import com.chronicare.platform.patients.domain.commands.DeletePatientCommand;
import com.chronicare.platform.patients.domain.commands.UpdatePatientCommand;

import java.util.Optional;

public interface PatientCommandService {
    Patient handle(CreatePatientCommand command);
    Patient handle(UpdatePatientCommand command);
    void handle(DeletePatientCommand command);
    
    /**
     * Assigns a doctor to a patient
     * @param patientId The patient ID
     * @param doctorId The doctor ID to assign
     * @return The updated patient
     */
    Patient handleAssignDoctor(Long patientId, Long doctorId);
    
    /**
     * Removes the assigned doctor from a patient
     * @param patientId The patient ID
     * @return The updated patient
     */
    Patient handleUnassignDoctor(Long patientId);
}
