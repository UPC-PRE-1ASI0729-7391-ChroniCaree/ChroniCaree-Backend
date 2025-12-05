package com.chronicare.platform.doctors.interfaces.rest.resources;

import java.util.List;

public record UpdateDoctorResource(
    String firstName,
    String lastName,
    String phone,
    String specialty,
    Double consultationFee,
    List<String> languages
) {}
