/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.users.domain.events;

import org.springframework.context.ApplicationEvent;

/**
 * Event fired when user is registered
 */
public class UserRegisteredEvent extends ApplicationEvent {

    private final Long userId;
    private final String email;

    public UserRegisteredEvent(Object source, Long userId, String email) {
        super(source);
        this.userId = userId;
        this.email = email;
    }

    public Long userId() {
        return userId;
    }

    public String email() {
        return email;
    }
}
