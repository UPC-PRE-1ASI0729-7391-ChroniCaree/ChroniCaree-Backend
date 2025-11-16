/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.users.domain.aggregates;

import com.example.users.domain.valueobjects.Email;
import com.example.users.domain.valueobjects.EncryptedPassword;
import com.example.users.domain.valueobjects.RoleVO;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Aggregate root: User
 */
@Getter
@Entity
@Table(name = "users", indexes = @Index(name = "idx_users_email", columnList = "email"))
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true, length = 150))
    })
    private Email email;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "password", nullable = false))
    })
    private EncryptedPassword password;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "role", nullable = false, length = 30))
    })
    private RoleVO role;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    @Column(name = "two_factor_enabled", nullable = false)
    private boolean twoFactorEnabled = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

 
    public User(Email email, EncryptedPassword password, RoleVO role, String name) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.createdAt = Instant.now();
    }

 
    public void markVerified() {
        if (!this.verified) {
            this.verified = true;
            this.updatedAt = Instant.now();
 
        }
    }

    public void enableTwoFactor() {
        this.twoFactorEnabled = true;
        this.updatedAt = Instant.now();
    }

    public void updateBasicInfo(String name, RoleVO role) {
        this.name = name != null ? name : this.name;
        this.role = role != null ? role : this.role;
        this.updatedAt = Instant.now();
    }

    public void changePassword(EncryptedPassword newPassword) {
        this.password = newPassword;
        this.updatedAt = Instant.now();
    }
}
