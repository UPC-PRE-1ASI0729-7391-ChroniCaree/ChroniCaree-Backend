/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.messages.infrastructure.persistence;

import com.example.messages.domain.model.Message;
import com.example.messages.domain.repository.MessageRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaMessageRepository extends JpaRepository<Message, Long>, MessageRepository {

    @Override
    List<Message> findByThreadId(Long threadId);

    @Override
    List<Message> findByReceiverId(Long receiverId);

    @Override
    List<Message> findBySenderId(Long senderId);

    @Override
    Optional<Message> findById(Long id);
}
