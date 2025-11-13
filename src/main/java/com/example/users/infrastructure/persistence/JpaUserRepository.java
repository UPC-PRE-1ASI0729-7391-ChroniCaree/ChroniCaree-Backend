/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.users.infrastructure.persistence;

import com.example.users.domain.model.User;
import com.example.users.domain.repository.UserRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

 
@Repository
public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {

    @Override
    Optional<User> findByEmail(String email);
    
}
