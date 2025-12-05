package com.chronicare.platform.doctors.domain.services;

import com.chronicare.platform.doctors.domain.model.aggregates.Doctor;
import com.chronicare.platform.doctors.domain.model.commands.*;

import java.util.Optional;

public interface DoctorCommandService {
    Optional<Doctor> handle(CreateDoctorCommand command);
    Optional<Doctor> handle(UpdateDoctorCommand command);
    Optional<Doctor> handle(VerifyDoctorCommand command);
    Optional<Doctor> handle(UpdateAcceptingPatientsCommand command);
    void handle(DeleteDoctorCommand command);
}
