package com.chronicare.platform.users.domain.commands;


public record EnableTwoFactorAuthCommand(Long userId) {

    public EnableTwoFactorAuthCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId invalid");
        }
    }
}
