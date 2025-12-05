package com.chronicare.platform.iam.interfaces.rest.resources;

/**
 * Authenticated User Resource
 * @summary Response DTO for successful authentication
 */
public record AuthenticatedUserResource(
    String accessToken, 
    String refreshToken, 
    UserInfo user
) {
    /**
     * User Info nested object with complete user data
     */
    public record UserInfo(
        Long id, 
        String email,
        String name,
        String role,
        Long tenantId,
        TenantInfo tenant,
        Long doctorId
    ) {}

    /**
     * Tenant Info nested object with tenant data
     */
    public record TenantInfo(
        Long id,
        String name,
        String status,
        Long subscriptionId,
        String email,
        String phone,
        String address,
        Boolean allowIndependentDoctors,
        Boolean requirePatientApproval,
        Integer maxDoctors
    ) {}
}
