/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.users.interfaces.rest;

import com.example.users.application.services.UserService;
import com.example.users.domain.aggregates.User;
import com.example.users.domain.commands.RegisterUserCommand;
import com.example.users.domain.commands.UpdateUserCommand;
import com.example.users.domain.commands.VerifyUserCommand;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserCommand cmd) {
        User created = service.register(cmd);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody UpdateUserCommand cmd) {
        UpdateUserCommand withId = new UpdateUserCommand(id, cmd.name(), cmd.role());
        return ResponseEntity.ok(service.update(withId));
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<Void> verify(@PathVariable Long id) {
        service.verify(new VerifyUserCommand(id));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
