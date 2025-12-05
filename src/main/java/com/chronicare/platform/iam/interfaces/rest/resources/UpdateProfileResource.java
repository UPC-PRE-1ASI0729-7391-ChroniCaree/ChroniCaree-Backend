package com.chronicare.platform.iam.interfaces.rest.resources;

/**
 * Update Profile Resource
 * @summary DTO for updating current user profile via PUT /authentication/me
 */
public record UpdateProfileResource(
    String name,
    String phone
) {}
