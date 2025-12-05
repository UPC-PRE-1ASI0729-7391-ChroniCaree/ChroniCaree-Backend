package com.chronicare.platform.doctors.interfaces.rest.transform;

import com.chronicare.platform.doctors.domain.model.commands.UpdateDoctorCommand;
import com.chronicare.platform.doctors.interfaces.rest.resources.UpdateDoctorResource;

public class UpdateDoctorCommandFromResourceAssembler {
    public static UpdateDoctorCommand toCommandFromResource(Long doctorId, UpdateDoctorResource resource) {
        return new UpdateDoctorCommand(
            doctorId,
            resource.firstName(),
            resource.lastName(),
            resource.phone(),
            resource.specialty(),
            resource.consultationFee(),
            resource.languages()
        );
    }
}
