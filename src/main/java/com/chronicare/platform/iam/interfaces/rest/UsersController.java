package com.chronicare.platform.iam.interfaces.rest;

import com.chronicare.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.chronicare.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.chronicare.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.chronicare.platform.iam.domain.model.queries.GetUsersByRoleQuery;
import com.chronicare.platform.iam.domain.model.valueobjects.Roles;
import com.chronicare.platform.iam.domain.services.UserCommandService;
import com.chronicare.platform.iam.domain.services.UserQueryService;
import com.chronicare.platform.iam.interfaces.rest.resources.CreateUserResource;
import com.chronicare.platform.iam.interfaces.rest.resources.UpdateUserResource;
import com.chronicare.platform.iam.interfaces.rest.resources.UserResource;
import com.chronicare.platform.iam.interfaces.rest.transform.RegisterUserCommandFromResourceAssembler;
import com.chronicare.platform.iam.interfaces.rest.transform.UpdateUserCommandFromResourceAssembler;
import com.chronicare.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.chronicare.platform.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UsersController
 * <p>
 *     This controller is responsible for handling all the requests related to users.
 *     It exposes the following endpoints:
 *     <ul>
 *         <li>POST /api/v1/users: Create a new user</li>
 *         <li>GET /api/v1/users: Get all users</li>
 *         <li>GET /api/v1/users/{userId}: Get user by ID</li>
 *         <li>GET /api/v1/users/email/{email}: Get user by email</li>
 *         <li>GET /api/v1/users/role/{role}: Get users by role</li>
 *         <li>PUT /api/v1/users/{userId}: Update user</li>
 *         <li>PATCH /api/v1/users/{userId}/verify: Verify user</li>
 *         <li>DELETE /api/v1/users/{userId}: Delete user</li>
 *     </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Available User Endpoints")
public class UsersController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    /**
     * Constructor
     * @param userCommandService The {@link UserCommandService} instance
     * @param userQueryService The {@link UserQueryService} instance
     */
    public UsersController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    /**
     * Create a new user
     * @param resource The {@link CreateUserResource} containing user data
     * @return A {@link UserResource} resource for the created user,
     *         or a bad request response if the user could not be created
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Register a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<UserResource> createUser(@Valid @RequestBody CreateUserResource resource) {
        var command = RegisterUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command);
        if (user.isEmpty()) return ResponseEntity.badRequest().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }

    /**
     * Get all users
     * @return A list of {@link UserResource} resources for all users
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var query = new GetAllUsersQuery();
        var users = userQueryService.handle(query);
        var userResources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(userResources);
    }

    /**
     * Get user by ID
     * @param userId The user ID
     * @return A {@link UserResource} resource for the user,
     *         or a not found response if the user could not be found
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var query = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(query);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Get user by email
     * @param email The user email address
     * @return A {@link UserResource} resource for the user,
     *         or a not found response if the user could not be found
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieve a user by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResource> getUserByEmail(@PathVariable String email) {
        var query = new GetUserByEmailQuery(email);
        var user = userQueryService.handle(query);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Check if email exists in the system
     * @param email The email address to check
     * @return A map containing "exists" boolean and optionally the user id if found
     */
    @GetMapping("/check-email")
    @Operation(summary = "Check email availability", description = "Check if an email is already registered in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email check completed")
    })
    public ResponseEntity<java.util.Map<String, Object>> checkEmailExists(@RequestParam String email) {
        var query = new GetUserByEmailQuery(email);
        var user = userQueryService.handle(query);
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("exists", user.isPresent());
        if (user.isPresent()) {
            result.put("userId", user.get().getId());
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Get users by role
     * @param role The role name (patient, doctor, hospital_admin)
     * @return A list of {@link UserResource} resources for all users with the specified role
     */
    @GetMapping("/role/{role}")
    @Operation(summary = "Get users by role", description = "Retrieve all users with a specific role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<UserResource>> getUsersByRole(@PathVariable String role) {
        var roleEnum = Roles.fromName(role);
        var query = new GetUsersByRoleQuery(roleEnum);
        var users = userQueryService.handle(query);
        var userResources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(userResources);
    }

    /**
     * Update user profile
     * @param userId The user ID
     * @param resource The {@link UpdateUserResource} containing updated user data
     * @return A {@link UserResource} resource for the updated user,
     *         or a not found response if the user could not be found
     */
    @PutMapping("/{userId}")
    @Operation(summary = "Update user", description = "Update user profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResource> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserResource resource) {
        var command = UpdateUserCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var user = userCommandService.handle(command);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Verify user
     * @param userId The user ID
     * @return A {@link UserResource} resource for the verified user,
     *         or a not found response if the user could not be found
     */
    @PatchMapping("/{userId}/verify")
    @Operation(summary = "Verify user", description = "Mark user as verified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User verified successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResource> verifyUser(@PathVariable Long userId) {
        var user = userCommandService.verifyUser(userId);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Delete user
     * @param userId The user ID
     * @return A success message if the user was deleted successfully
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user", description = "Delete a user from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<MessageResource> deleteUser(@PathVariable Long userId) {
        userCommandService.deleteUser(userId);
        return ResponseEntity.ok(new MessageResource("User with given id successfully deleted"));
    }
}
