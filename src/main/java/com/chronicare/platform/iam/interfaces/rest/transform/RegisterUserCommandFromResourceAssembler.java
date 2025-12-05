package com.chronicare.platform.iam.interfaces.rest.transform;

import com.chronicare.platform.iam.domain.model.commands.RegisterUserCommand;
import com.chronicare.platform.iam.domain.model.valueobjects.Roles;
import com.chronicare.platform.iam.interfaces.rest.resources.CreateUserResource;

/**
 * Assembler to convert CreateUserResource to RegisterUserCommand
 */
public class RegisterUserCommandFromResourceAssembler {
    
    public static RegisterUserCommand toCommandFromResource(CreateUserResource resource) {
        var role = Roles.fromName(resource.role());
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
