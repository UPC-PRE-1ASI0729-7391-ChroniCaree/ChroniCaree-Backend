package com.chronicare.platform.iam.interfaces.rest.transform;

import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token, String refreshToken) {
        var userInfo = new AuthenticatedUserResource.UserInfo(
            user.getId(), 
            user.getEmailAddress(), 
            user.getRole().getName()
        );
        return new AuthenticatedUserResource(token, refreshToken, userInfo);
    }
}
