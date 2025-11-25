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
}
