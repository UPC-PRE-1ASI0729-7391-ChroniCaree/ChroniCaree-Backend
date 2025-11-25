package com.chronicare.platform.iam.interfaces.rest.transform;

import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.interfaces.rest.resources.UserResource;

/**
 * Assembler to convert User entity to UserResource
 */
public class UserResourceFromEntityAssembler {
    
    public static UserResource toResourceFromEntity(User entity) {
        return new UserResource(
            entity.getId(),
            entity.getEmailAddress(),
            entity.getName(),
            entity.getRole().getName(),
            entity.getTenantId(),
            entity.getIsVerified(),
            entity.getTwoFactorEnabled(),
            entity.getCreatedAt().toString()
        );
    }
}
