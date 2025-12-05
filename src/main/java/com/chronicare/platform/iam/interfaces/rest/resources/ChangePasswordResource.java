package com.chronicare.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Change Password Resource
 * @summary DTO for changing user password
 */
public record ChangePasswordResource(
    @NotBlank(message = "La contraseña actual es requerida")
    String oldPassword,
    
    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    String newPassword
) {}
