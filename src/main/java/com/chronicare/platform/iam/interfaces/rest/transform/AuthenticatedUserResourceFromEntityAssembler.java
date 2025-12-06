package com.chronicare.platform.iam.interfaces.rest.transform;

import com.chronicare.platform.doctors.domain.model.aggregates.Doctor;
import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.chronicare.platform.tenants.domain.aggregates.Tenant;

public class AuthenticatedUserResourceFromEntityAssembler {
    
    /**
     * Convert User entity to AuthenticatedUserResource
     * @param user The user entity
     * @param token The JWT access token
     * @param refreshToken The refresh token
     * @return AuthenticatedUserResource with basic user info only
     */
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token, String refreshToken) {
        var userInfo = new AuthenticatedUserResource.UserInfo(
            user.getId(), 
            user.getEmailAddress(),
            user.getName(),
            user.getRole().getName().toUpperCase(),
            user.getTenantId(),
            null, // tenant not provided
            null  // doctorId not provided
        );
        return new AuthenticatedUserResource(token, refreshToken, userInfo);
    }

    /**
     * Convert User entity to AuthenticatedUserResource with Tenant and Doctor info
     * @param user The user entity
     * @param token The JWT access token
     * @param refreshToken The refresh token
     * @param tenant The tenant entity (can be null)
     * @param doctor The doctor entity (can be null)
     * @return AuthenticatedUserResource with full user, tenant and doctor info
     */
    public static AuthenticatedUserResource toResourceFromEntity(
            User user, 
            String token, 
            String refreshToken,
            Tenant tenant,
            Doctor doctor
    ) {
        // Build tenant info if tenant is provided
        AuthenticatedUserResource.TenantInfo tenantInfo = null;
        if (tenant != null) {
            tenantInfo = new AuthenticatedUserResource.TenantInfo(
                tenant.getId(),
                tenant.getName() != null ? tenant.getName().value() : null,
                tenant.getStatus(),
                tenant.getSubscriptionId(),
                tenant.getEmail(),
                tenant.getPhone(),
                tenant.getAddress(),
                tenant.getAllowIndependentDoctors(),
                tenant.getRequirePatientApproval(),
                tenant.getMaxDoctors()
            );
        }

        // Get doctorId if doctor is provided
        Long doctorId = doctor != null ? doctor.getId() : null;

        var userInfo = new AuthenticatedUserResource.UserInfo(
            user.getId(), 
            user.getEmailAddress(),
            user.getName(),
            user.getRole().getName().toUpperCase(),
            user.getTenantId(),
            tenantInfo,
            doctorId
        );
        return new AuthenticatedUserResource(token, refreshToken, userInfo);
    }
}
