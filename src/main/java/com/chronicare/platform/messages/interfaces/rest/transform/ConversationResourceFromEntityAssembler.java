package com.chronicare.platform.messages.interfaces.rest.transform;

import com.chronicare.platform.messages.domain.model.aggregates.Conversation;
import com.chronicare.platform.messages.domain.model.valueobjects.Participant;
import com.chronicare.platform.messages.interfaces.rest.resources.ConversationResource;
import org.springframework.stereotype.Component;

/**
 * Assembler for converting Conversation aggregate to ConversationResource
 */
@Component
public class ConversationResourceFromEntityAssembler {

    public ConversationResource toResource(Conversation conversation) {
        ConversationResource resource = new ConversationResource();
        
        resource.setId(conversation.getId());
        resource.setType(conversation.getType() != null ? conversation.getType().toString() : null);
        resource.setTitle(conversation.getTitle());
        resource.setDescription(conversation.getDescription());
        resource.setTenantId(conversation.getTenantId());
        resource.setCreatedAt(conversation.getCreatedAt());
        resource.setLastMessageAt(conversation.getLastMessageAt());
        resource.setMessageCount(conversation.getMessageCount());
        
        if (conversation.getParticipants() != null) {
            resource.setParticipants(conversation.getParticipants().stream()
                    .map(this::toParticipantResource)
                    .toList());
        }
        
        return resource;
    }

    private ConversationResource.ParticipantResource toParticipantResource(Participant participant) {
        ConversationResource.ParticipantResource resource = new ConversationResource.ParticipantResource();
        resource.setUserId(participant.getUserId());
        resource.setRole(participant.getRole() != null ? participant.getRole().toString() : null);
        resource.setJoinedAt(participant.getJoinedAt());
        return resource;
    }
}
