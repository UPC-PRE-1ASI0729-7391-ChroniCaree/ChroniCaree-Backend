package com.chronicare.platform.iam.interfaces.rest.resources;

/**
 * Authenticated User Resource
 * @summary Response DTO for successful authentication
 */
public record AuthenticatedUserResource(
    String accessToken, 
    String refreshToken, 
    UserInfo user
) {
    /**
     * User Info nested object
     */
    public record UserInfo(
        Long id, 
        String email, 
        String role
    ) {}
}
