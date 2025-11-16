/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.users.domain.commands;

import com.example.users.domain.valueobjects.Email;
import com.example.users.domain.valueobjects.RoleVO;

/**
 * Command to register a user
 */
public record RegisterUserCommand(Email email, String rawPassword, String name, RoleVO.Role role) {

    public RegisterUserCommand    {
        if (email == null || email.getValue().isBlank()) {
            throw new IllegalArgumentException("email required");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("password required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        if (role == null) {
            throw new IllegalArgumentException("role required");
        }
    }
}
