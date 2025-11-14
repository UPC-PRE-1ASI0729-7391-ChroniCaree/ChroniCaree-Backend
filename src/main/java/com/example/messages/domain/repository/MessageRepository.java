/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.messages.domain.repository;

import com.example.messages.domain.model.Message;
import java.util.List;
import java.util.Optional;

public interface MessageRepository {

    List<Message> findAll();

    Optional<Message> findById(Long id);

    List<Message> findByThreadId(Long threadId);

    List<Message> findByReceiverId(Long receiverId);

    List<Message> findBySenderId(Long senderId);

    Message save(Message message);

    void deleteById(Long id);
}
