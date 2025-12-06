package com.chronicare.platform.messages.application.service;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import com.chronicare.platform.messages.domain.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Application Service for Message Queries (New DDD Implementation)
 */
@Service("messageQueryServiceNewImpl")
public class MessageQueryServiceImpl implements MessageQueryService {

    private final MessageRepository messageRepository;

    public MessageQueryServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Message getMessageById(String messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> listMessages(String conversationId, int page, int limit) {
        return messageRepository.findByConversationIdPaginated(conversationId, page, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getMessagesByConversation(String conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getMessagesBySender(String conversationId, Long senderId) {
        return messageRepository.findByConversationIdAndSenderId(conversationId, senderId);
    }
}
