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
 * Users Controller
 * @summary REST API for managing users
 * @description Exposes endpoints for user CRUD operations
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User management endpoints")
public class UsersController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UsersController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    /**
     * Create a new user
     */
    @Operation(summary = "Create a new user", description = "Register a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<UserResource> createUser(@Valid @RequestBody CreateUserResource resource) {
        var command = RegisterUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command);
        
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }

    /**
     * Get all users
     */
    @Operation(summary = "Get all users", description = "Retrieve all registered users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
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
     */
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var query = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(query);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Get user by email
     */
    @Operation(summary = "Get user by email", description = "Retrieve a user by email address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResource> getUserByEmail(@PathVariable String email) {
        var query = new GetUserByEmailQuery(email);
        var user = userQueryService.handle(query);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Get users by role
     */
    @Operation(summary = "Get users by role", description = "Retrieve all users with specific role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/role/{role}")
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
     */
    @Operation(summary = "Update user", description = "Update user profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<UserResource> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserResource resource) {
        
        var command = UpdateUserCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var user = userCommandService.handle(command);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Verify user
     */
    @Operation(summary = "Verify user", description = "Mark user as verified")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User verified successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{userId}/verify")
    public ResponseEntity<UserResource> verifyUser(@PathVariable Long userId) {
        var user = userCommandService.verifyUser(userId);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Delete user
     */
    @Operation(summary = "Delete user", description = "Delete a user from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<MessageResource> deleteUser(@PathVariable Long userId) {
        userCommandService.deleteUser(userId);
        return ResponseEntity.ok(new MessageResource("User deleted successfully"));
    }
}
