package com.chronicare.platform.iam.infrastructure.authorization.sfs.services;

import com.chronicare.platform.iam.domain.model.aggregates.User;
import com.chronicare.platform.iam.domain.model.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail_Address(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return com.chronicare.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl.build(user);
    }
}
