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
public interface JpaMessageRepository extends JpaRepository<Message, String>, MessageRepository {

    @Override
    List<Message> findByThreadId(String threadId);

    @Override
    List<Message> findByReceiverId(String receiverId);

    @Override
    List<Message> findBySenderId(String senderId);

    @Override
    Optional<Message> findById(String id);
}
