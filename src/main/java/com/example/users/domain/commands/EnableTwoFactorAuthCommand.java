/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.users.domain.commands;


public record EnableTwoFactorAuthCommand(Long userId) {

    public EnableTwoFactorAuthCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId invalid");
        }
    }
}
