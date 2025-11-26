package com.chronicare.platform.iam.interfaces.rest;

import com.chronicare.platform.iam.domain.services.UserCommandService;
import com.chronicare.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
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

/**
 * AuthenticationController
 * <p>
 *     This controller is responsible for handling all the requests related to authentication.
 *     It exposes the following endpoints:
 *     <ul>
 *         <li>POST /api/v1/authentication/sign-in: Sign in a user</li>
 *         <li>POST /api/v1/authentication/sign-up: Register a new user</li>
 *     </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {
    private final UserCommandService userCommandService;

    /**
     * Constructor
     * @param userCommandService The {@link UserCommandService} instance
     */
    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
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
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource resource) {
        var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var authenticatedUser = userCommandService.handle(command);
        if (authenticatedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                authenticatedUser.get().getLeft(), 
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
    public ResponseEntity<UserResource> signUp(@RequestBody CreateUserResource resource) {
        var command = RegisterUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }
}
