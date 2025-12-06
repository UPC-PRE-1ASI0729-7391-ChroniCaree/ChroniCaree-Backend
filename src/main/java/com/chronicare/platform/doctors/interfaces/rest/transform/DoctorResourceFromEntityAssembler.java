package com.chronicare.platform.doctors.interfaces.rest.transform;

import com.chronicare.platform.doctors.domain.model.aggregates.Doctor;
import com.chronicare.platform.doctors.domain.model.valueobjects.Education;
import com.chronicare.platform.doctors.interfaces.rest.resources.DoctorResource;
import com.chronicare.platform.doctors.interfaces.rest.resources.EducationResource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

public class DoctorResourceFromEntityAssembler {
    public static DoctorResource toResourceFromEntity(Doctor doctor) {
        var educationResources = doctor.getEducation().stream()
            .map(edu -> new EducationResource(edu.getDegree(), edu.getInstitution(), edu.getYear()))
            .collect(Collectors.toList());

        return new DoctorResource(
            doctor.getId(),
            doctor.getUserId(),
            doctor.getTenantId(),
            doctor.getName().getFirstName(),
            doctor.getName().getLastName(),
            doctor.getFullName(),
            doctor.getDni().getValue(),
            doctor.getSpecialty().getValue(),
            doctor.getLicenseNumber().getValue(),
            doctor.getPhone().getValue(),
            doctor.getIsIndependent(),
            doctor.getIsVerified(),
            doctor.getAcceptingPatients(),
            doctor.getConsultationFee(),
            doctor.getLanguages(),
            educationResources,
            doctor.canAcceptPatients(),
            doctor.belongsToTenant(),
            doctor.getCreatedAt()
        );
    }
}
