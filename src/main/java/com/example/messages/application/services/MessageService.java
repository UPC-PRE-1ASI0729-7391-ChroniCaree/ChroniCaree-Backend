/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.messages.application.services;

import com.example.messages.domain.model.Message;
import com.example.messages.domain.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(String id) {
        return messageRepository.findById(id);
    }

    public List<Message> getMessagesByThread(String threadId) {
        return messageRepository.findByThreadId(threadId);
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message markAsRead(String id) {
        return messageRepository.findById(id)
                .map(msg -> {
                    msg.setRead(true);
                    return messageRepository.save(msg);
                })
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }

    public void deleteMessage(String id) {
        messageRepository.deleteById(id);
    }
}
