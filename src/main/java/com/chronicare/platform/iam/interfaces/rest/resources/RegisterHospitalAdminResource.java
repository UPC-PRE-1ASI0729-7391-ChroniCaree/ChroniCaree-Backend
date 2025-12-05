package com.chronicare.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Resource for registering a Hospital Admin with their Hospital (Tenant)
 * Creates both User (hospital_admin) and Tenant in a single transaction
 * 
 * Supports multiple field naming conventions from frontend:
 * - email OR adminEmail
 * - password OR adminPassword
 * - name OR adminName OR firstName/lastName
 * - hospitalName
 * - hospitalPhone OR phone
 * - hospitalAddress OR address
 * - hospitalEmail
 * - ruc (optional)
 */
public record RegisterHospitalAdminResource(
    // User data - supports: email, adminEmail
    // Note: Email validation is done at the service level via getEmail() to support flexible field names
    String email,
    String adminEmail,
    
    // Password - supports: password, adminPassword
    String password,
    String adminPassword,
    
    // User name - supports: name, adminName, firstName/lastName
    String name,
    String adminName,
    String firstName,
    String lastName,
    
    // Hospital (Tenant) data
    @NotBlank(message = "Hospital name is required")
    String hospitalName,
    
    // Hospital contact - supports multiple formats
    String hospitalEmail,
    String hospitalPhone,
    String phone,
    String hospitalAddress,
    String address,
    
    // Optional fields
    String ruc
) {
    /**
     * Get the admin email (supports both 'email' and 'adminEmail' fields)
     */
    public String getEmail() {
        if (email != null && !email.isBlank()) {
            return email;
        }
        return adminEmail != null ? adminEmail : "";
    }
    
    /**
     * Get the admin password (supports both 'password' and 'adminPassword' fields)
     */
    public String getPassword() {
        if (password != null && !password.isBlank()) {
            return password;
        }
        return adminPassword != null ? adminPassword : "";
    }
    
    /**
     * Get the full name for the admin user
     * Supports: name, adminName, firstName/lastName
     */
    public String getFullName() {
        // Try firstName/lastName first
        if (firstName != null && !firstName.isBlank()) {
            String full = firstName;
            if (lastName != null && !lastName.isBlank()) {
                full = full + " " + lastName;
            }
            return full;
        }
        // Try adminName
        if (adminName != null && !adminName.isBlank()) {
            return adminName;
        }
        // Fallback to name
        return name != null ? name : "";
    }
    
    /**
     * Get hospital phone (supports both 'hospitalPhone' and 'phone' fields)
     */
    public String getHospitalPhone() {
        if (hospitalPhone != null && !hospitalPhone.isBlank()) {
            return hospitalPhone;
        }
        return phone != null ? phone : "";
    }
    
    /**
     * Get hospital address (supports both 'hospitalAddress' and 'address' fields)
     */
    public String getHospitalAddress() {
        if (hospitalAddress != null && !hospitalAddress.isBlank()) {
            return hospitalAddress;
        }
        return address != null ? address : "";
    }
    
    /**
     * Get hospital email
     */
    public String getHospitalEmail() {
        return hospitalEmail != null ? hospitalEmail : getEmail();
    }
    
    /**
     * Get RUC (optional)
     */
    public String getRuc() {
        return ruc != null ? ruc : "";
    }
    
    /**
     * Validate that required fields are present
     */
    public boolean isValid() {
        return !getEmail().isBlank() && 
               !getPassword().isBlank() && 
               hospitalName != null && !hospitalName.isBlank();
    }
}
