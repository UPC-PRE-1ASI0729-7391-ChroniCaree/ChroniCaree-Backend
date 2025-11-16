/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.users.domain.repository;
 
import com.example.users.domain.aggregates.User;
import com.example.users.domain.valueobjects.Email;
import java.util.List;
import java.util.Optional;

 
public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(Email email);

    List<User> findAll();

    User save(User user);

    void deleteById(Long id);
    
}
