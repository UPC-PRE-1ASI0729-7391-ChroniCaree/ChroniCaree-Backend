package com.chronicare.platform.iam.application.internal.commandservices;

import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.commands.RegisterUserCommand;
import com.chronicare.platform.iam.domain.model.commands.UpdateUserCommand;
import com.chronicare.platform.iam.domain.model.repositories.UserRepository;
import com.chronicare.platform.iam.domain.services.UserCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * User Command Service Implementation
 * @summary Handles user commands
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;

    public UserCommandServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Optional<User> handle(RegisterUserCommand command) {
        // Check if email already exists
        if (userRepository.existsByEmail_Address(command.email())) {
            throw new IllegalArgumentException("Email already exists: " + command.email());
        }

        // TODO: Hash password before saving (use BCryptPasswordEncoder in production)
        var user = new User(command);
        var savedUser = userRepository.save(user);
        return Optional.of(savedUser);
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
