/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.users.domain.events;

import org.springframework.context.ApplicationEvent;

public class UserVerifiedEvent extends ApplicationEvent {

    private final Long userId;

    public UserVerifiedEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }

    public Long userId() {
        return userId;
    }
}
