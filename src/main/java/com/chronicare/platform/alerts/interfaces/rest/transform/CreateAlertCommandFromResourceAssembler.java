package com.chronicare.platform.alerts.interfaces.rest.transform;

import com.chronicare.platform.alerts.domain.model.commands.CreateAlertCommand;
import com.chronicare.platform.alerts.interfaces.rest.resources.CreateAlertResource;

/**
 * Assembler to convert CreateAlertResource to CreateAlertCommand
 */
public class CreateAlertCommandFromResourceAssembler {
    
    private CreateAlertCommandFromResourceAssembler() {
        throw new UnsupportedOperationException("Utility class");
    }
    
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
            "MANUAL", // source
            resource.sourceType(),
            resource.sourceId(),
            java.time.LocalDateTime.now(), // detectedAt
            resource.metadata(),
            resource.priority(),
            resource.expiresAt()
        );
    }
}
