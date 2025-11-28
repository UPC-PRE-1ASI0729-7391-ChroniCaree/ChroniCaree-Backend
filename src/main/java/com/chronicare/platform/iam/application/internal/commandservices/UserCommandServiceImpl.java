package com.chronicare.platform.iam.application.internal.commandservices;

import com.chronicare.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.chronicare.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.chronicare.platform.iam.infrastructure.tokens.RefreshTokenService;
import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.commands.RegisterUserCommand;
import com.chronicare.platform.iam.domain.model.commands.SignInCommand;
import com.chronicare.platform.iam.domain.model.commands.UpdateUserCommand;
import com.chronicare.platform.iam.domain.model.repositories.UserRepository;
import com.chronicare.platform.iam.domain.services.UserCommandService;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * User Command Service Implementation
 * @summary Handles user commands
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {
    private static final Logger logger = Logger.getLogger(UserCommandServiceImpl.class.getName());
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    @Transactional
    public Optional<User> handle(RegisterUserCommand command) {
        logger.info("Handling RegisterUserCommand for email: " + command.email());
        // Check if email already exists
        if (userRepository.existsByEmail_Address(command.email())) {
            logger.warning("Email already exists: " + command.email());
            throw new IllegalArgumentException("Email already exists: " + command.email());
        }

        var user = new User(
                command.email(),
                hashingService.encode(command.password()),
                command.name(),
                command.role(),
                command.tenantId()
        );
        var savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: " + savedUser.getId());
        return Optional.of(savedUser);
    }

    @Override
    @Transactional
    public Optional<ImmutableTriple<User, String, String>> handle(SignInCommand command) {
        logger.info("Handling SignInCommand for username: " + command.username());
        var user = userRepository.findByEmail_Address(command.username());
        if (user.isEmpty()) {
            logger.warning("User not found for username: " + command.username());
            return Optional.empty();
        }
        if (!hashingService.matches(command.password(), user.get().getPassword())) {
            logger.warning("Password mismatch for username: " + command.username());
            return Optional.empty();
        }
        var token = tokenService.generateToken(user.get().getUsername());
        var refreshToken = refreshTokenService.createRefreshToken(user.get().getId());
        logger.info("User authenticated successfully: " + command.username());
        return Optional.of(ImmutableTriple.of(user.get(), token, refreshToken.getToken()));
    }

    @Override
    @Transactional
    public Optional<User> handle(UpdateUserCommand command) {
        var userOptional = userRepository.findById(command.userId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + command.userId());
        }

        var user = userOptional.get();
        user.update(command);
        var updatedUser = userRepository.save(user);
        return Optional.of(updatedUser);
    }

    @Override
    @Transactional
    public Optional<User> verifyUser(Long userId) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        var user = userOptional.get();
        user.verify();
        var verifiedUser = userRepository.save(user);
        return Optional.of(verifiedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
