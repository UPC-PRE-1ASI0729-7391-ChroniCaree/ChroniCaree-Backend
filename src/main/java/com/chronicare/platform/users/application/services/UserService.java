/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.users.application.services;

import com.chronicare.platform.users.domain.aggregates.User;
import com.chronicare.platform.users.domain.commands.RegisterUserCommand;
import com.chronicare.platform.users.domain.commands.UpdateUserCommand;
import com.chronicare.platform.users.domain.commands.VerifyUserCommand;
import com.chronicare.platform.users.domain.valueobjects.Email;
import com.chronicare.platform.users.domain.valueobjects.EncryptedPassword;
import com.chronicare.platform.users.domain.valueobjects.RoleVO;
import com.chronicare.platform.users.infrastructure.persistence.JpaUserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final JpaUserRepository repository;
    private final ApplicationEventPublisher events;

    public UserService(JpaUserRepository repository, ApplicationEventPublisher events) {
        this.repository = repository;
        this.events = events;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public Optional<User> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<User> getByEmail(Email email) {
        return repository.findByEmail(email);
    }

    public User register(RegisterUserCommand cmd) {

        // Construir el VO primero
        Email email = cmd.email();

        // Validar duplicados usando el VO
        repository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("email already in use");
        });

        EncryptedPassword pwd = new EncryptedPassword(cmd.rawPassword(), false);
        RoleVO role = new RoleVO(cmd.role());
        User user = new User(email, pwd, role, cmd.name());

        User saved = repository.save(user);
        events.publishEvent(
                new com.chronicare.platform.users.domain.events.UserRegisteredEvent(
                        this,
                        saved.getId(),
                        saved.getEmail().getValue()
                )
        );
        return saved;
    }

    public User update(UpdateUserCommand cmd) {
        User existing = repository.findById(cmd.userId()).orElseThrow(() -> new IllegalArgumentException("user not found"));
        RoleVO roleVO = cmd.role() != null ? new RoleVO(cmd.role()) : null;
        existing.updateBasicInfo(cmd.name(), roleVO);
        User saved = repository.save(existing);
        events.publishEvent(new com.chronicare.platform.users.domain.events.UserUpdatedEvent(this, saved.getId()));
        return saved;
    }

    public void verify(VerifyUserCommand cmd) {
        User existing = repository.findById(cmd.userId()).orElseThrow(() -> new IllegalArgumentException("user not found"));
        existing.markVerified();
        repository.save(existing);
        events.publishEvent(new com.chronicare.platform.users.domain.events.UserVerifiedEvent(this, existing.getId()));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
