package com.chronicare.platform.doctors.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.List;

public record DoctorResource(
    Long id,
    Long userId,
    Long tenantId,
    String firstName,
    String lastName,
    String fullName,
    String dni,
    String specialty,
    String licenseNumber,
    String phone,
    Boolean isIndependent,
    Boolean isVerified,
    Boolean acceptingPatients,
    Double consultationFee,
    List<String> languages,
    List<EducationResource> education,
    Boolean canAcceptPatients,
    Boolean belongsToTenant,
    LocalDateTime joinedAt
) {}
