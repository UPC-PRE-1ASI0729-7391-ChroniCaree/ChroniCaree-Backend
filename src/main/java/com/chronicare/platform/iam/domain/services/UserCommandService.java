package com.chronicare.platform.iam.domain.services;

import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.commands.RegisterHospitalAdminCommand;
import com.chronicare.platform.iam.domain.model.commands.RegisterUserCommand;
import com.chronicare.platform.iam.domain.model.commands.SignInCommand;
import com.chronicare.platform.iam.domain.model.commands.UpdateUserCommand;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.Optional;

/**
 * User Command Service
 * @summary Interface for handling user commands
 */
public interface UserCommandService {
    
    /**
     * Register a new user
     */
    Optional<User> handle(RegisterUserCommand command);
    
    /**
     * Register a Hospital Admin with their Hospital (Tenant)
     * Creates both User and Tenant in a single transaction
     * @return Pair of (User, TenantId)
     */
    Optional<ImmutablePair<User, Long>> handle(RegisterHospitalAdminCommand command);
    
    /**
     * Update user profile
     */
    Optional<User> handle(UpdateUserCommand command);
    
    /**
     * Verify user by ID
     */
    Optional<User> verifyUser(Long userId);
    
    /**
     * Delete user by ID
     */
    void deleteUser(Long userId);

    /**
     * Sign in user
     */
    Optional<ImmutableTriple<User, String, String>> handle(SignInCommand command);

    /**
     * Change user password
     * @param userId The user ID
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if password changed successfully
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * Update user tenant ID
     */
    Optional<User> updateUserTenantId(Long userId, Long tenantId);
}
