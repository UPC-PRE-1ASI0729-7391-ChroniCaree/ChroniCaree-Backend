package com.chronicare.platform.iam.domain.model.queries;

import com.chronicare.platform.iam.domain.model.valueobjects.Roles;

/**
 * Summary: Query to get users by role
 */
public record GetUsersByRoleQuery(Roles role) {
}
