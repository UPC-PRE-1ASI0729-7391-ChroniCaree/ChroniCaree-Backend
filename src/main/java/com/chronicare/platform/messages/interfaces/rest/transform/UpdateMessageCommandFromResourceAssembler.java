package com.chronicare.platform.messages.interfaces.rest.transform;

import com.chronicare.platform.messages.domain.model.commands.UpdateMessageCommand;
import com.chronicare.platform.messages.interfaces.rest.resources.UpdateMessageResource;

/**
 * Assembler to convert UpdateMessageResource to UpdateMessageCommand
 */
public class UpdateMessageCommandFromResourceAssembler {

    public static UpdateMessageCommand toCommandFromResource(Long id, UpdateMessageResource resource) {
        return new UpdateMessageCommand(
                id,
                resource.body(),
                resource.subject(),
                resource.isUrgent()
        );
    }
}
