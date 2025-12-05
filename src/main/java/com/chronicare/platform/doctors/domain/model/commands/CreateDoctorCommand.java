package com.chronicare.platform.doctors.domain.model.commands;

import java.util.List;

public record CreateDoctorCommand(
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
    List<EducationData> education
) {
    public record EducationData(String degree, String institution, Integer year) {}
}
