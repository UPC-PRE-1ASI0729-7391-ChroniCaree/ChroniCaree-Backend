package com.chronicare.platform.iam.domain.model.commands;

/**
 * Command to register a Hospital Admin with their Hospital (Tenant)
 * This creates both entities in a single transaction
 */
public record RegisterHospitalAdminCommand(
    // User data
    String email,
    String password,
    String name,
    
    // Hospital (Tenant) data
    String hospitalName,
    String hospitalEmail,
    String hospitalPhone,
    String hospitalAddress
) {
}
