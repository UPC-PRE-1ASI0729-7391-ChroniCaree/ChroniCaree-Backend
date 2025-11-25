package com.chronicare.platform.iam.domain.model.repositories;

import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.valueobjects.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository
 * @summary Domain repository interface for User aggregate
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address
     */
    Optional<User> findByEmail_Address(String email);

    /**
     * Find all users by role
     */
    List<User> findAllByRole(Roles role);

    /**
     * Find all users by tenant
     */
    List<User> findAllByTenantId(Long tenantId);

    /**
     * Check if email exists
     */
    boolean existsByEmail_Address(String email);

    /**
     * Find all verified users
     */
    List<User> findAllByIsVerified(Boolean isVerified);
}
