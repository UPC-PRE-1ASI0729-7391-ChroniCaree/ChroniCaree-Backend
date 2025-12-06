package com.chronicare.platform.iam.domain.model.commands;

import com.chronicare.platform.iam.domain.model.valueobjects.Roles;

/**
 * Summary: Command to register a new user
 * @param email User email
 * @param password Plain text password (will be hashed)
 * @param name Full name
 * @param role User role
 */
public record RegisterUserCommand(
    String email,
    String password,
    String name,
    Roles role,
    Long tenantId
) {
}
