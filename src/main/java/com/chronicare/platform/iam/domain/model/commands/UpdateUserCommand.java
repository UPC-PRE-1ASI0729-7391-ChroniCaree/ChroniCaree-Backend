package com.chronicare.platform.iam.domain.model.commands;

/**
 * Command to update user profile
 */
public record UpdateUserCommand(
    Long userId,
    String name,
    Boolean isVerified,
    Boolean twoFactorEnabled
) {
}
