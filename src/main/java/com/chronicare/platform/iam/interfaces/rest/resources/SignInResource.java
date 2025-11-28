package com.chronicare.platform.iam.interfaces.rest.resources;

/**
 * Sign In Resource
 * @summary DTO for user sign in - accepts email as username
 */
public record SignInResource(String email, String password) {
}
