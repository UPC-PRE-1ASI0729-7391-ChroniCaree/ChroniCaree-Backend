package com.chronicare.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Create User Resource
 * @summary DTO for creating a new user
 * Supports both name field (legacy) and firstName/lastName (frontend format)
 */
public record CreateUserResource(
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,
    
    @NotBlank(message = "Password is required")
    String password,
    
    // Legacy field - optional when firstName/lastName are provided
    String name,
    
    // Frontend format fields
    String firstName,
    String lastName,
    
    @NotNull(message = "Role is required")
    String role,
    
    Long tenantId
) {
    /**
     * Get the full name, combining firstName and lastName if provided,
     * otherwise using the name field
     */
    public String getFullName() {
        if (firstName != null && !firstName.isBlank()) {
            String full = firstName;
            if (lastName != null && !lastName.isBlank()) {
                full = full + " " + lastName;
            }
            return full;
        }
        return name != null ? name : "";
    }
}
