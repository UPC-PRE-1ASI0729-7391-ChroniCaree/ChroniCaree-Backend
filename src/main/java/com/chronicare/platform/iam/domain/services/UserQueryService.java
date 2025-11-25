package com.chronicare.platform.iam.domain.services;

import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.chronicare.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.chronicare.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.chronicare.platform.iam.domain.model.queries.GetUsersByRoleQuery;

import java.util.List;
import java.util.Optional;

/**
 * User Query Service
 * @summary Interface for handling user queries
 */
public interface UserQueryService {
    
    /**
     * Get all users
     */
    List<User> handle(GetAllUsersQuery query);
    
    /**
     * Get user by ID
     */
    Optional<User> handle(GetUserByIdQuery query);
    
    /**
     * Get user by email
     */
    Optional<User> handle(GetUserByEmailQuery query);
    
    /**
     * Get users by role
     */
    List<User> handle(GetUsersByRoleQuery query);
}
