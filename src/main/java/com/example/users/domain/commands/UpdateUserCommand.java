/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.users.domain.commands;

import com.example.users.domain.valueobjects.RoleVO;

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
