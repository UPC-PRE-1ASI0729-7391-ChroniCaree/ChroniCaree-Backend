package com.chronicare.platform.users.domain.commands;

/**
 * Command to verify a user
 */
public record VerifyUserCommand(Long userId) {
    public VerifyUserCommand {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("userId invalid");
    }
}
