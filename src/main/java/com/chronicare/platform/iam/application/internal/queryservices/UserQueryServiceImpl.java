package com.chronicare.platform.iam.application.internal.queryservices;

import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.chronicare.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.chronicare.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.chronicare.platform.iam.domain.model.queries.GetUsersByRoleQuery;
import com.chronicare.platform.iam.domain.model.repositories.UserRepository;
import com.chronicare.platform.iam.domain.services.UserQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * User Query Service Implementation
 * @summary Handles user queries
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail_Address(query.email());
    }

    @Override
    public List<User> handle(GetUsersByRoleQuery query) {
        return userRepository.findAllByRole(query.role());
    }
}
