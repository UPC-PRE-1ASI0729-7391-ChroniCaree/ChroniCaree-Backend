package com.chronicare.platform.iam.interfaces.rest.resources;

/**
 * User Resource
 * @summary DTO for exposing user data via REST API
 */
public record UserResource(
    Long id,
    String email,
    String name,
    String role,
    Long tenantId,
    Boolean isVerified,
    Boolean twoFactorEnabled,
    String createdAt
) {
}
