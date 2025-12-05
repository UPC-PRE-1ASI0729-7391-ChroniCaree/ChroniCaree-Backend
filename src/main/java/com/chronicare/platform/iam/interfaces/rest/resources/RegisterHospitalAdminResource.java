package com.chronicare.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Resource for registering a Hospital Admin with their Hospital (Tenant)
 * Creates both User (hospital_admin) and Tenant in a single transaction
 */
public record RegisterHospitalAdminResource(
    // User data
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,
    
    @NotBlank(message = "Password is required")
    String password,
    
    // User name - supports both formats
    String name,
    String firstName,
    String lastName,
    
    // Hospital (Tenant) data
    @NotBlank(message = "Hospital name is required")
    String hospitalName,
    
    String hospitalEmail,
    String hospitalPhone,
    String hospitalAddress
) {
    /**
     * Get the full name for the admin user
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
