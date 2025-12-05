package com.chronicare.platform.iam.interfaces.rest;

import com.chronicare.platform.doctors.domain.model.aggregates.Doctor;
import com.chronicare.platform.doctors.infrastructure.persistence.jpa.repositories.DoctorRepository;
import com.chronicare.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.entities.RefreshToken;
import com.chronicare.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.chronicare.platform.iam.domain.services.UserCommandService;
import com.chronicare.platform.iam.domain.services.UserQueryService;
import com.chronicare.platform.iam.infrastructure.tokens.RefreshTokenService;
import com.chronicare.platform.iam.infrastructure.tokens.jwt.JwtTokenService;
import com.chronicare.platform.iam.interfaces.rest.resources.*;
import com.chronicare.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.chronicare.platform.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.chronicare.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.chronicare.platform.iam.interfaces.rest.transform.RegisterUserCommandFromResourceAssembler;
import com.chronicare.platform.shared.interfaces.rest.resources.ApiErrorResponse;
import com.chronicare.platform.tenants.domain.aggregates.Tenant;
import com.chronicare.platform.tenants.domain.repository.TenantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * AuthenticationController
 * <p>
 *     This controller is responsible for handling all the requests related to authentication.
 *     It exposes the following endpoints:
 *     <ul>
 *         <li>POST /api/v1/authentication/sign-in: Sign in a user</li>
 *         <li>POST /api/v1/authentication/sign-up: Register a new user</li>
 *         <li>POST /api/v1/authentication/refresh: Refresh access token</li>
 *         <li>POST /api/v1/authentication/logout: Logout user</li>
 *         <li>GET /api/v1/authentication/me: Get current user</li>
 *         <li>PUT /api/v1/authentication/me: Update current user profile</li>
 *         <li>POST /api/v1/authentication/change-password: Change password</li>
 *     </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {
    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;
    private final TenantRepository tenantRepository;
    private final DoctorRepository doctorRepository;

    public AuthenticationController(
            UserCommandService userCommandService, 
            UserQueryService userQueryService,
            RefreshTokenService refreshTokenService, 
            JwtTokenService jwtTokenService,
            TenantRepository tenantRepository,
            DoctorRepository doctorRepository) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenService = jwtTokenService;
        this.tenantRepository = tenantRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Sign in a user
     * @param resource The {@link SignInResource} containing email and password
     * @return The {@link AuthenticatedUserResource} with user data and tokens
     */
    @PostMapping("/sign-in")
    @Operation(summary = "Sign in a user", description = "Authenticate a user with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInResource resource) {
        logger.info("Received sign-in request for user: " + resource.email());
        try {
            var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
            var authenticatedUser = userCommandService.handle(command);
            if (authenticatedUser.isEmpty()) {
                logger.warning("Sign-in failed: Invalid credentials for " + resource.email());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiErrorResponse("AUTH_INVALID_CREDENTIALS", "Email o contraseña incorrectos"));
            }
            
            User user = authenticatedUser.get().getLeft();
            // Generate token with full claims
            String token = jwtTokenService.generateTokenFromUser(user);
            String refreshToken = authenticatedUser.get().getRight();
            
            // Get tenant info for hospital_admin or if user has tenantId
            logger.info("Fetching tenant information...");
            logger.info("User role: " + user.getRole().getName());
            logger.info("User tenantId: " + user.getTenantId());
            
            Tenant tenant = null;
            if (user.getTenantId() != null) {
                logger.info("Finding tenant by tenantId: " + user.getTenantId());
                tenant = tenantRepository.findById(user.getTenantId()).orElse(null);
                if (tenant != null) {
                    logger.info("✓ Tenant found by ID: " + tenant.getName());
                } else {
                    logger.warning("❌ Tenant NOT FOUND by ID: " + user.getTenantId());
                }
            } else if ("HOSPITAL_ADMIN".equalsIgnoreCase(user.getRole().getName())) {
                // Hospital admin: find tenant by adminUserId
                logger.info("User is HOSPITAL_ADMIN, finding tenant by adminUserId: " + user.getId());
                tenant = tenantRepository.findByAdminUserId(user.getId()).orElse(null);
                if (tenant != null) {
                    logger.info("✓ Tenant found by adminUserId: " + tenant.getName() + " (ID: " + tenant.getId() + ")");
                } else {
                    logger.warning("❌ Tenant NOT FOUND by adminUserId: " + user.getId());
                }
            }
            
            // Get doctor info if user is a doctor
            Doctor doctor = null;
            if ("DOCTOR".equalsIgnoreCase(user.getRole().getName())) {
                doctor = doctorRepository.findByUserId(user.getId()).orElse(null);
            }
            
            logger.info("Sign-in successful for user: " + resource.email());
            var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                    user, token, refreshToken, tenant, doctor
            );
            return ResponseEntity.ok(authenticatedUserResource);
        } catch (Exception e) {
            logger.severe("Sign-in error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("AUTH_INTERNAL_ERROR", "Error interno de autenticación"));
        }
    }

    /**
     * Register a new user
     * @param resource The {@link CreateUserResource} containing user registration data
     * @return The {@link UserResource} for the created user
     */
    @PostMapping("/sign-up")
    @Operation(summary = "Register a new user", description = "Create a new user account with specified role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Conflict - Email already exists")
    })
    public ResponseEntity<?> signUp(@RequestBody @Valid CreateUserResource resource) {
        logger.info("Received sign-up request for email: " + resource.email());
        try {
            var command = RegisterUserCommandFromResourceAssembler.toCommandFromResource(resource);
            var user = userCommandService.handle(command);
            if (user.isEmpty()) {
                logger.warning("Sign-up failed for email: " + resource.email());
                return ResponseEntity.badRequest()
                        .body(new ApiErrorResponse("VALIDATION_ERROR", "No se pudo registrar el usuario"));
            }
            logger.info("Sign-up successful for email: " + resource.email());
            var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
            return new ResponseEntity<>(userResource, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.warning("Sign-up conflict: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorResponse("CONFLICT", e.getMessage()));
        } catch (Exception e) {
            logger.severe("Sign-up error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("AUTH_INTERNAL_ERROR", "Error interno de registro"));
        }
    }

    /**
     * Register a Hospital Admin with their Hospital
     * Creates both User (hospital_admin role) and Tenant (hospital) in a single transaction
     * @param resource The {@link RegisterHospitalAdminResource} containing admin and hospital data
     * @return The authenticated user resource with tokens
     */
    @PostMapping("/sign-up/hospital-admin")
    @Operation(summary = "Register Hospital Admin", description = "Create a hospital admin account with their hospital in one transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hospital Admin and Hospital created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Conflict - Email or Hospital name already exists")
    })
    public ResponseEntity<?> signUpHospitalAdmin(@RequestBody @Valid RegisterHospitalAdminResource resource) {
        logger.info("========== HOSPITAL ADMIN SIGN-UP START ==========");
        logger.info("Email: " + resource.email());
        logger.info("Name: " + resource.getFullName());
        logger.info("Hospital Name: " + resource.hospitalName());
        logger.info("Hospital Email: " + resource.hospitalEmail());
        logger.info("Hospital Phone: " + resource.hospitalPhone());
        logger.info("Hospital Address: " + resource.hospitalAddress());
        try {
            var command = new com.chronicare.platform.iam.domain.model.commands.RegisterHospitalAdminCommand(
                resource.email(),
                resource.password(),
                resource.getFullName(),
                resource.hospitalName(),
                resource.hospitalEmail(),
                resource.hospitalPhone(),
                resource.hospitalAddress()
            );
            logger.info("Command created, calling UserCommandService.handle()");
            
            var result = userCommandService.handle(command);
            if (result.isEmpty()) {
                logger.severe("❌ Hospital admin sign-up failed - result is empty");
                return ResponseEntity.badRequest()
                        .body(new ApiErrorResponse("VALIDATION_ERROR", "No se pudo registrar el administrador del hospital"));
            }
            
            User user = result.get().getLeft();
            Long tenantId = result.get().getRight();
            
            logger.info("✓ User created successfully:");
            logger.info("  - User ID: " + user.getId());
            logger.info("  - Email: " + user.getEmailAddress());
            logger.info("  - Role: " + user.getRole().getName());
            logger.info("  - Tenant ID (in User): " + user.getTenantId());
            logger.info("✓ Tenant created successfully:");
            logger.info("  - Tenant ID (returned): " + tenantId);
            
            // Generate tokens
            logger.info("Generating JWT token...");
            String token = jwtTokenService.generateTokenFromUser(user);
            logger.info("✓ JWT token generated (length: " + token.length() + ")");
            
            logger.info("Generating refresh token...");
            String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();
            logger.info("✓ Refresh token generated (length: " + refreshToken.length() + ")");
            
            // Get tenant for response
            logger.info("Fetching tenant by ID: " + tenantId);
            Tenant tenant = tenantRepository.findById(tenantId).orElse(null);
            if (tenant != null) {
                logger.info("✓ Tenant found:");
                logger.info("  - Tenant ID: " + tenant.getId());
                logger.info("  - Name: " + tenant.getName());
                logger.info("  - Admin User ID: " + tenant.getAdminUserId());
                logger.info("  - Email: " + tenant.getEmail());
                logger.info("  - Status: " + tenant.getStatus());
            } else {
                logger.severe("❌ CRITICAL: Tenant NOT FOUND after creation! TenantId: " + tenantId);
            }
            
            // Build response with full tenant info
            var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                user, token, refreshToken, tenant, null
            );
            
            logger.info("========== HOSPITAL ADMIN SIGN-UP SUCCESS ==========");
            logger.info("✓ User Email: " + resource.email());
            logger.info("✓ User ID: " + user.getId());
            logger.info("✓ Tenant ID: " + tenantId);
            logger.info("✓ Token generated: YES");
            logger.info("✓ Response prepared, returning HTTP 201");
            logger.info("====================================================");
            return new ResponseEntity<>(authenticatedUserResource, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.severe("❌ Hospital admin sign-up CONFLICT: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorResponse("CONFLICT", e.getMessage()));
        } catch (Exception e) {
            logger.severe("❌ Hospital admin sign-up CRITICAL ERROR: " + e.getMessage());
            logger.severe("Exception type: " + e.getClass().getName());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("AUTH_INTERNAL_ERROR", "Error interno de registro: " + e.getMessage()));
        }
    }

    /**
     * Get current authenticated user
     * @return Current user information
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get the authenticated user's profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiErrorResponse("AUTH_TOKEN_INVALID", "Token inválido o no proporcionado"));
            }
            
            String username = auth.getName();
            var userOpt = userQueryService.handle(new com.chronicare.platform.iam.domain.model.queries.GetUserByEmailQuery(username));
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiErrorResponse("AUTH_USER_NOT_FOUND", "Usuario no encontrado"));
            }
            
            User user = userOpt.get();
            var currentUser = new CurrentUserResource(
                    user.getId(),
                    user.getEmailAddress(),
                    user.getName(),
                    user.getRole().getName().toLowerCase(),
                    user.getTenantId(),
                    user.getIsVerified()
            );
            return ResponseEntity.ok(currentUser);
        } catch (Exception e) {
            logger.severe("Get current user error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("INTERNAL_ERROR", "Error al obtener usuario"));
        }
    }

    /**
     * Update current user profile
     * @param resource Profile update data
     * @return Updated user information
     */
    @PutMapping("/me")
    @Operation(summary = "Update current user profile", description = "Update the authenticated user's profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> updateCurrentUser(@RequestBody UpdateProfileResource resource) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiErrorResponse("AUTH_TOKEN_INVALID", "Token inválido"));
            }
            
            String username = auth.getName();
            var userOpt = userQueryService.handle(new com.chronicare.platform.iam.domain.model.queries.GetUserByEmailQuery(username));
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiErrorResponse("AUTH_USER_NOT_FOUND", "Usuario no encontrado"));
            }
            
            User user = userOpt.get();
            var updateCommand = new com.chronicare.platform.iam.domain.model.commands.UpdateUserCommand(
                    user.getId(),
                    resource.name(),
                    null, // isVerified unchanged
                    null  // twoFactorEnabled unchanged
            );
            
            var updatedUser = userCommandService.handle(updateCommand);
            if (updatedUser.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiErrorResponse("VALIDATION_ERROR", "No se pudo actualizar el perfil"));
            }
            
            var currentUser = new CurrentUserResource(
                    updatedUser.get().getId(),
                    updatedUser.get().getEmailAddress(),
                    updatedUser.get().getName(),
                    updatedUser.get().getRole().getName().toLowerCase(),
                    updatedUser.get().getTenantId(),
                    updatedUser.get().getIsVerified()
            );
            return ResponseEntity.ok(currentUser);
        } catch (Exception e) {
            logger.severe("Update profile error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("INTERNAL_ERROR", "Error al actualizar perfil"));
        }
    }

    /**
     * Change user password
     * @param resource Old and new password
     * @return Success message
     */
    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Change the authenticated user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid old password"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordResource resource) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiErrorResponse("AUTH_TOKEN_INVALID", "Token inválido"));
            }
            
            String username = auth.getName();
            var userOpt = userQueryService.handle(new com.chronicare.platform.iam.domain.model.queries.GetUserByEmailQuery(username));
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiErrorResponse("AUTH_USER_NOT_FOUND", "Usuario no encontrado"));
            }
            
            boolean success = userCommandService.changePassword(
                    userOpt.get().getId(),
                    resource.oldPassword(),
                    resource.newPassword()
            );
            
            if (!success) {
                return ResponseEntity.badRequest()
                        .body(new ApiErrorResponse("AUTH_INVALID_CREDENTIALS", "Contraseña actual incorrecta"));
            }
            
            return ResponseEntity.ok(Map.of(
                    "code", "PASSWORD_CHANGED",
                    "message", "Contraseña actualizada correctamente"
            ));
        } catch (Exception e) {
            logger.severe("Change password error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("INTERNAL_ERROR", "Error al cambiar contraseña"));
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Get a new access token using a refresh token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenResource resource) {
        try {
            var tokenOpt = refreshTokenService.findByToken(resource.refreshToken());
            if (tokenOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiErrorResponse("AUTH_REFRESH_INVALID", "Refresh token inválido o expirado"));
            }
            
            var verifiedToken = refreshTokenService.verifyExpiration(tokenOpt.get());
            var user = verifiedToken.getUser();
            
            String token = jwtTokenService.generateTokenFromUser(user);
            refreshTokenService.revokeToken(tokenOpt.get());
            RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
            
            return ResponseEntity.ok(AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                    user, token, newRefreshToken.getToken()
            ));
        } catch (Exception e) {
            logger.severe("Refresh token error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiErrorResponse("AUTH_REFRESH_INVALID", "Refresh token inválido o expirado"));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Revoke refresh token")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenResource resource) {
        refreshTokenService.findByToken(resource.refreshToken())
                .ifPresent(refreshTokenService::revokeToken);
        return ResponseEntity.ok(Map.of(
                "code", "LOGOUT_SUCCESS",
                "message", "Sesión cerrada correctamente"
        ));
    }
}
