package com.chronicare.platform.doctors.interfaces.rest.transform;

import com.chronicare.platform.doctors.domain.model.commands.CreateDoctorCommand;
import com.chronicare.platform.doctors.interfaces.rest.resources.CreateDoctorResource;

import java.util.stream.Collectors;

public class CreateDoctorCommandFromResourceAssembler {
    public static CreateDoctorCommand toCommandFromResource(CreateDoctorResource resource) {
        var educationData = resource.education() != null
            ? resource.education().stream()
                .map(edu -> new CreateDoctorCommand.EducationData(
                    edu.degree(),
                    edu.institution(),
                    edu.year()
                ))
                .collect(Collectors.toList())
            : null;

        return new CreateDoctorCommand(
            resource.userId(),
            resource.tenantId(),
            resource.firstName(),
            resource.lastName(),
            resource.dni(),
            resource.specialty(),
            resource.licenseNumber(),
            resource.phone(),
            resource.consultationFee(),
            resource.languages(),
            educationData
        );
    }
}
