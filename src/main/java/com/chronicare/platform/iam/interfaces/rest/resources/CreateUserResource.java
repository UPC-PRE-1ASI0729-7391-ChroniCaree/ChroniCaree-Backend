package com.chronicare.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Create User Resource
 * @summary DTO for creating a new user
 */
public record CreateUserResource(
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,
    
    @NotBlank(message = "Password is required")
    String password,
    
    @NotBlank(message = "Name is required")
    String name,
    
    @NotNull(message = "Role is required")
    String role,
    
    Long tenantId
) {
}
