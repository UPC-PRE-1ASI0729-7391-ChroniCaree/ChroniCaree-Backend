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

    Optional<Message> findById(String id);

    List<Message> findByThreadId(String threadId);

    List<Message> findByReceiverId(String receiverId);

    List<Message> findBySenderId(String senderId);

    Message save(Message message);

    void deleteById(String id);
}
