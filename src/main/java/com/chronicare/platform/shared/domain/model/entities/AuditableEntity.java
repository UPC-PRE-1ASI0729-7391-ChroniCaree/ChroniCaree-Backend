package com.chronicare.platform.shared.domain.model.entities;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private Long createdBy;
    private LocalDateTime createdAt;

    private Long updatedBy;
    private LocalDateTime updatedAt;
}
