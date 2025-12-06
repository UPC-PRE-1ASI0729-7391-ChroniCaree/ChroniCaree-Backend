package com.chronicare.platform.shared.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent {
    private final UUID eventId;
    private final LocalDateTime occurredAt;

    public DomainEvent() {
        this.eventId = UUID.randomUUID();
        this.occurredAt = LocalDateTime.now();
    }

    public UUID getEventId() {
        return eventId;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
