package com.chronicare.platform.users.domain.commands;

import com.chronicare.platform.users.domain.valueobjects.RoleVO;

/**
 * Command to update user basic info (partial)
 */
public record UpdateUserCommand(Long userId, String name, RoleVO.Role role) {

    public UpdateUserCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        if (role == null) {
            throw new IllegalArgumentException("role required");
        }
    }
}
