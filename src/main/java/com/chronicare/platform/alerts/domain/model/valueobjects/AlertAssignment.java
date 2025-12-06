package com.chronicare.platform.alerts.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Alert Assignment - tracks who is assigned to handle the alert
 */
@Embeddable
public class AlertAssignment {
    private Long userId;
    private String role; // DOCTOR, NURSE, etc.

    public AlertAssignment() {}

    public AlertAssignment(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
