package com.chronicare.platform.messages.application.service;

import com.chronicare.platform.messages.domain.model.aggregates.Conversation;
import com.chronicare.platform.messages.domain.model.valueobjects.Participant;
import com.chronicare.platform.messages.domain.model.valueobjects.ConversationType;
import com.chronicare.platform.messages.domain.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Application Service for Conversation Commands (DDD Implementation)
 */
@Service
public class ConversationCommandServiceImpl implements ConversationCommandService {

    private final ConversationRepository conversationRepository;

    public ConversationCommandServiceImpl(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    @Transactional
    public Conversation createConversation(Long tenantId, String type, List<Long> participantUserIds) {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID().toString());
        conversation.setTenantId(tenantId);
        
        // Set conversation type
        try {
            conversation.setType(ConversationType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException _) {
            conversation.setType(ConversationType.GROUP);
        }
        
        conversation.setCreatedAt(Instant.now());
        
        Set<Participant> participants = new HashSet<>();
        for (Long userId : participantUserIds) {
            Participant participant = new Participant();
            participant.setUserId(userId);
            participant.setRole("PATIENT");
            participant.setJoinedAt(Instant.now());
            participants.add(participant);
        }
        conversation.setParticipants(participants);
        
        return conversationRepository.save(conversation);
    }

    @Override
    @Transactional
    public Conversation updateConversation(String conversationId, String title, String description) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        
        if (title != null) {
            conversation.setTitle(title);
        }
        if (description != null) {
            conversation.setDescription(description);
        }
        
        return conversationRepository.save(conversation);
    }

    @Override
    @Transactional
    public void deleteConversation(String conversationId) {
        conversationRepository.delete(conversationId);
    }
}
