package com.chronicare.platform.iam.interfaces.rest.resources;

/**
 * Update User Resource
 * @summary DTO for updating user profile
 */
public record UpdateUserResource(
    String name,
    Boolean isVerified,
    Boolean twoFactorEnabled
) {
}
