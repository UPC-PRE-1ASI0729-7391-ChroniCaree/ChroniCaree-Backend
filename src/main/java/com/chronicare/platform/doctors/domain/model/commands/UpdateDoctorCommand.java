package com.chronicare.platform.doctors.domain.model.commands;

import java.util.List;

public record UpdateDoctorCommand(
    Long doctorId,
    String firstName,
    String lastName,
    String phone,
    String specialty,
    Double consultationFee,
    List<String> languages
) {}
