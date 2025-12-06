package com.chronicare.platform.messages.application.service;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import com.chronicare.platform.messages.domain.model.valueobjects.ContentType;
import com.chronicare.platform.messages.domain.model.valueobjects.MessageStatus;
import com.chronicare.platform.messages.domain.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Application Service for Message Commands (New DDD Implementation)
 */
@Service("messageCommandServiceNewImpl")
public class MessageCommandServiceImpl implements MessageCommandService {

    private final MessageRepository messageRepository;

    public MessageCommandServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public Message createMessage(String conversationId, Long senderId, Long tenantId, String body, String contentType) {
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setConversationId(conversationId);
        message.setSenderId(senderId);
        message.setTenantId(tenantId);
        message.setBody(body);
        
        try {
            message.setContentType(ContentType.valueOf(contentType.toUpperCase()));
        } catch (IllegalArgumentException _) {
            message.setContentType(ContentType.TEXT);
        }
        
        message.setStatus(MessageStatus.QUEUED);
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        
        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public Message editMessage(String messageId, String newBody) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        
        message.setBody(newBody);
        message.setEditedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        
        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public Message updateMessageStatus(String messageId, String statusString) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        
        try {
            MessageStatus status = MessageStatus.valueOf(statusString.toUpperCase());
            message.setStatus(status);
        } catch (IllegalArgumentException _) {
            // Invalid status, keep the current status
        }
        
        message.setUpdatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public void deleteMessage(String messageId) {
        messageRepository.delete(messageId);
    }
}
