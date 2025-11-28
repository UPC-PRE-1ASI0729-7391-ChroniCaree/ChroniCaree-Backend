package com.chronicare.platform.shared.domain.model.aggregates;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Base class for all aggregate roots that require auditing
 * @param <T> The type of the aggregate root
 * @summary 
 * The class is an abstract class that extends the {@link AbstractAggregateRoot} class 
 * and adds auditing fields for tracking creation and modification timestamps.
 * It also provides methods for registering domain events.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditableAbstractAggregateRoot<T extends AbstractAggregateRoot<T>>
extends AbstractAggregateRoot<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Registers a domain event
     * @param event The domain event to register
     */
    public void addDomainEvent(Object event) {
        super.registerEvent(event);
    }
}
