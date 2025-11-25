package com.chronicare.platform.iam.interfaces.rest.transform;

import com.chronicare.platform.iam.domain.model.commands.UpdateUserCommand;
import com.chronicare.platform.iam.interfaces.rest.resources.UpdateUserResource;

/**
 * Assembler to convert UpdateUserResource to UpdateUserCommand
 */
public class UpdateUserCommandFromResourceAssembler {
    
    public static UpdateUserCommand toCommandFromResource(Long userId, UpdateUserResource resource) {
        return new UpdateUserCommand(
            userId,
            resource.name(),
            resource.isVerified(),
            resource.twoFactorEnabled()
        );
    }
}
