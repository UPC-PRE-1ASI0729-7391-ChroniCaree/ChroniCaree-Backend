package com.chronicare.platform.alerts.infrastructure.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Service for publishing domain events to the Spring application event bus
 */
@Service
public class DomainEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(DomainEventPublisher.class);
    
    private final ApplicationEventPublisher applicationEventPublisher;

    public DomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(Object event) {
        try {
            logger.info("Publishing domain event: {}", event.getClass().getSimpleName());
            applicationEventPublisher.publishEvent(event);
        } catch (Exception ex) {
            logger.error("Error publishing domain event", ex);
        }
    }
}
