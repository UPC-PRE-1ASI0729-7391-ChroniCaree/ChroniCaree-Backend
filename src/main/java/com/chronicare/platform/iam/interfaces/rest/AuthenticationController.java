package com.chronicare.platform.iam.interfaces.rest;

import com.chronicare.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.chronicare.platform.iam.domain.model.entities.RefreshToken;
import com.chronicare.platform.iam.domain.services.UserCommandService;
import com.chronicare.platform.iam.infrastructure.tokens.RefreshTokenService;
import com.chronicare.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.chronicare.platform.iam.interfaces.rest.resources.RefreshTokenResource;
import com.chronicare.platform.iam.interfaces.rest.resources.SignInResource;
import com.chronicare.platform.iam.interfaces.rest.resources.UserResource;
import com.chronicare.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.chronicare.platform.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.chronicare.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.chronicare.platform.iam.interfaces.rest.resources.CreateUserResource;
import com.chronicare.platform.iam.interfaces.rest.transform.RegisterUserCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
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
 *     </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {
    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());
    private final UserCommandService userCommandService;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    /**
     * Constructor
     * @param userCommandService The {@link UserCommandService} instance
     * @param refreshTokenService The {@link RefreshTokenService} instance
     * @param tokenService The {@link TokenService} instance
     */
    public AuthenticationController(UserCommandService userCommandService, RefreshTokenService refreshTokenService, TokenService tokenService) {
        this.userCommandService = userCommandService;
        this.refreshTokenService = refreshTokenService;
        this.tokenService = tokenService;
    }

    /**
     * Sign in a user
     * @param resource The {@link SignInResource} containing email and password
     * @return The {@link AuthenticatedUserResource} with user data and token,
     *         or a 404 response if credentials are invalid
     */
    @PostMapping("/sign-in")
    @Operation(summary = "Sign in a user", description = "Authenticate a user with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found or invalid credentials")
    })
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody @Valid SignInResource resource) {
        logger.info("Received sign-in request for user: " + resource.email());
        var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var authenticatedUser = userCommandService.handle(command);
        if (authenticatedUser.isEmpty()) {
            logger.warning("Sign-in failed: User not found or invalid credentials for " + resource.email());
            return ResponseEntity.notFound().build();
        }
        logger.info("Sign-in successful for user: " + resource.email());
        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                authenticatedUser.get().getLeft(), 
                authenticatedUser.get().getMiddle(),
                authenticatedUser.get().getRight()
        );
        return ResponseEntity.ok(authenticatedUserResource);
    }

    /**
     * Register a new user
     * @param resource The {@link CreateUserResource} containing user registration data
     * @return The {@link UserResource} for the created user,
     *         or a 400 response if registration fails
     */
    @PostMapping("/sign-up")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data or email already exists")
    })
    public ResponseEntity<UserResource> signUp(@RequestBody @Valid CreateUserResource resource) {
        logger.info("Received sign-up request for email: " + resource.email());
        var command = RegisterUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command);
        if (user.isEmpty()) {
            logger.warning("Sign-up failed for email: " + resource.email());
            return ResponseEntity.badRequest().build();
        }
        logger.info("Sign-up successful for email: " + resource.email());
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Get a new access token using a refresh token")
    public ResponseEntity<AuthenticatedUserResource> refreshToken(@RequestBody RefreshTokenResource resource) {
        return refreshTokenService.findByToken(resource.refreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = tokenService.generateToken(user.getUsername(), user.getRole().getName());
                    // Rotate refresh token: revoke old one (or delete) and create new one
                    // Here we just create a new one and let the old one expire or we can delete it.
                    // Requirement says: "Rotación de refresh tokens (cada refresh se emite uno nuevo y se revoca el anterior)"
                    
                    // Find the old token object again to revoke/delete it
                    Optional<RefreshToken> oldToken = refreshTokenService.findByToken(resource.refreshToken());
                    oldToken.ifPresent(refreshTokenService::revokeToken);
                    
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
                    
                    return ResponseEntity.ok(AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                            user,
                            token,
                            newRefreshToken.getToken()
                    ));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Revoke refresh token")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenResource resource) {
        refreshTokenService.findByToken(resource.refreshToken())
                .ifPresent(refreshTokenService::revokeToken);
        return ResponseEntity.ok("Log out successful!");
    }
}
