package com.chronicare.platform.iam.domain.services;

import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.commands.RegisterUserCommand;
import com.chronicare.platform.iam.domain.model.commands.SignInCommand;
import com.chronicare.platform.iam.domain.model.commands.UpdateUserCommand;
import org.apache.commons.lang3.tuple.ImmutablePair;

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
    Optional<ImmutablePair<User, String>> handle(SignInCommand command);
}
