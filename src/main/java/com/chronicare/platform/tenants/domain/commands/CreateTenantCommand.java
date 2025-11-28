package com.chronicare.platform.tenants.domain.commands;

import com.chronicare.platform.tenants.domain.valueobjects.TenantName;
import java.time.LocalDateTime;

/**
 * Command para crear un tenant.
 */
public record CreateTenantCommand(
    Long adminUserId,
    TenantName name,
    String email,
    String address,
    String phone,
    String status,
    Long subscriptionId,
    LocalDateTime registrationDate,
    Boolean allowIndependentDoctors,
    Boolean requirePatientApproval,
    Integer maxDoctors
) {

    public CreateTenantCommand {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
    }
}
