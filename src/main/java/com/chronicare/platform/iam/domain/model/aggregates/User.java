package com.chronicare.platform.iam.domain.model.aggregates;

import com.chronicare.platform.iam.domain.model.commands.RegisterUserCommand;
import com.chronicare.platform.iam.domain.model.commands.UpdateUserCommand;
import com.chronicare.platform.iam.domain.model.valueobjects.EmailAddress;
import com.chronicare.platform.iam.domain.model.valueobjects.Roles;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * User Aggregate Root
 * @summary Represents a user in the IAM bounded context
 * @description Handles user registration, authentication, and profile management
 */
@Entity
@Table(name = "users")
@Getter
public class User extends AuditableAbstractAggregateRoot<User> {

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "address", column = @Column(name = "email", unique = true, nullable = false))
    })
    private EmailAddress email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is required")
    @Column(nullable = false)
    private Roles role;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "two_factor_enabled", nullable = false)
    private Boolean twoFactorEnabled = false;

    protected User() {
        // Required by JPA
    }

    public User(String email, String password, String name, Roles role, Long tenantId) {
        this();
        this.email = new EmailAddress(email);
        this.password = password;
        this.name = name;
        this.role = role;
        this.tenantId = tenantId;
        this.isVerified = false;
        this.twoFactorEnabled = false;
    }

    public String getUsername() {
        return this.email.address();
    }

    /**
     * Update user profile
     */
    public User update(UpdateUserCommand command) {
        if (command.name() != null && !command.name().isBlank()) {
            this.name = command.name();
        }
        if (command.isVerified() != null) {
            this.isVerified = command.isVerified();
        }
        if (command.twoFactorEnabled() != null) {
            this.twoFactorEnabled = command.twoFactorEnabled();
        }
        return this;
    }

    /**
     * Verify user email
     */
    public void verify() {
        this.isVerified = true;
    }

    /**
     * Enable two-factor authentication
     */
    public void enableTwoFactor() {
        this.twoFactorEnabled = true;
    }

    /**
     * Disable two-factor authentication
     */
    public void disableTwoFactor() {
        this.twoFactorEnabled = false;
    }

    /**
     * Update password (should receive already hashed password)
     */
    public void updatePassword(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = hashedPassword;
    }

    /**
     * Check if user has specific role
     */
    public boolean hasRole(Roles role) {
        return this.role == role;
    }

    /**
     * Check if user belongs to tenant
     */
    public boolean belongsToTenant(Long tenantId) {
        return this.tenantId != null && this.tenantId.equals(tenantId);
    }

    /**
     * Get email as string
     */
    public String getEmailAddress() {
        return email.address();
    }
}
