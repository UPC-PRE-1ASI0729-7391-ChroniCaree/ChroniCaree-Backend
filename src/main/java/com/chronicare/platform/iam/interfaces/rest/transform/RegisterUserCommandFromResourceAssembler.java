package com.chronicare.platform.iam.interfaces.rest.transform;

import com.chronicare.platform.iam.domain.model.commands.RegisterUserCommand;
import com.chronicare.platform.iam.domain.model.valueobjects.Roles;
import com.chronicare.platform.iam.interfaces.rest.resources.CreateUserResource;

/**
 * Assembler to convert CreateUserResource to RegisterUserCommand
 */
public class RegisterUserCommandFromResourceAssembler {
    
    public static RegisterUserCommand toCommandFromResource(CreateUserResource resource) {
        // Default to PATIENT role if not provided
        String roleName = resource.role() != null && !resource.role().isBlank() 
            ? resource.role() 
            : "PATIENT";
        var role = Roles.fromName(roleName);
        // Use getFullName() to support both name and firstName/lastName formats
        String fullName = resource.getFullName();
        return new RegisterUserCommand(
            resource.email(),
            resource.password(),
            fullName,
            role,
            resource.tenantId()
        );
    }
}
