package com.chronicare.platform.alerts.interfaces.rest.transform;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.commands.CreateAlertCommand;
import com.chronicare.platform.alerts.interfaces.rest.resources.CreateAlertResource;

/**
 * Assembler to convert CreateAlertResource to CreateAlertCommand
 */
public class CreateAlertCommandFromResourceAssembler {
    
    public static CreateAlertCommand toCommandFromResource(CreateAlertResource resource) {
        return new CreateAlertCommand(
            resource.patientId(),
            resource.doctorId(),
            resource.tenantId(),
            resource.type(),
            resource.severity(),
            resource.category(),
            resource.title(),
            resource.message(),
            resource.description(),
            resource.sourceType(),
            resource.sourceId(),
            resource.metadata(),
            resource.priority(),
            resource.expiresAt()
        );
    }
}
