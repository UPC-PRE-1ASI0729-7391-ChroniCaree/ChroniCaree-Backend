package com.chronicare.platform.doctors.interfaces.rest.resources;

import java.util.List;

public record CreateDoctorResource(
    Long userId,
    Long tenantId,
    String firstName,
    String lastName,
    String dni,
    String specialty,
    String licenseNumber,
    String phone,
    Double consultationFee,
    List<String> languages,
    List<EducationResource> education
) {}
