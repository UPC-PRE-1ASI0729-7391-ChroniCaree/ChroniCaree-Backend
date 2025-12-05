package com.chronicare.platform.iam.interfaces.rest.resources;

/**
 * Current User Resource
 * @summary DTO for GET /authentication/me response
 */
public record CurrentUserResource(
    Long id,
    String email,
    String name,
    String role,
    Long tenantId,
    Boolean isVerified
) {}
