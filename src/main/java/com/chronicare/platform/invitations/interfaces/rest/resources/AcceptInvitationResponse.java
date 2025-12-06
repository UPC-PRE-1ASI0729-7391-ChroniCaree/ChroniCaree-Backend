package com.chronicare.platform.invitations.interfaces.rest.resources;

/**
 * Summary: Response for accepting an invitation
 */
public record AcceptInvitationResponse(
    boolean success,
    UserResponse user,
    DoctorResponse doctor,
    String accessToken,
    String refreshToken
) {
    public record UserResponse(
        Long id,
        String email,
        String role,
        String name
    ) {}

    public record DoctorResponse(
        Long id,
        Long userId,
        Long tenantId,
        String firstName,
        String lastName,
        String specialty,
        String licenseNumber
    ) {}
}
