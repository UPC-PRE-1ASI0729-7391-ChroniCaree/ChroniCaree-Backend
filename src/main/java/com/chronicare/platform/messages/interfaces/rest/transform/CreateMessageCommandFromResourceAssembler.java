package com.chronicare.platform.messages.interfaces.rest.transform;

import com.chronicare.platform.messages.domain.model.commands.CreateMessageCommand;
import com.chronicare.platform.messages.interfaces.rest.resources.CreateMessageResource;

/**
 * Assembler to convert CreateMessageResource to CreateMessageCommand
 */
public class CreateMessageCommandFromResourceAssembler {

    public static CreateMessageCommand toCommandFromResource(CreateMessageResource resource) {
        return new CreateMessageCommand(
                resource.threadId(),
                resource.senderRole(),
                resource.senderId(),
                resource.receiverId(),
                resource.body(),
                resource.subject(),
                resource.isUrgent()
        );
    }
}
