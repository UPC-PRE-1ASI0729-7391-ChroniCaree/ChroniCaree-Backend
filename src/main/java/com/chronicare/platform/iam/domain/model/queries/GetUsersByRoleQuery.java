package com.chronicare.platform.iam.domain.model.queries;

import com.chronicare.platform.iam.domain.model.valueobjects.Roles;

/**
 * Query to get users by role
 */
public record GetUsersByRoleQuery(Roles role) {
}
