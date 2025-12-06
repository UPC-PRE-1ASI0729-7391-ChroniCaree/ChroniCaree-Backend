package com.chronicare.platform.attachments.domain.model.aggregates;

import com.chronicare.platform.attachments.domain.model.commands.CreateAttachmentCommand;
import com.chronicare.platform.attachments.domain.model.commands.UpdateAttachmentCommand;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Summary: Attachment Aggregate Root
 * Represents a file attachment in the system
 */
@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String mimeType;

    private String url;

    private Long sizeBytes;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ========== Constructors from Commands ==========

    public Attachment(CreateAttachmentCommand command) {
        this.fileName = command.fileName();
        this.mimeType = command.mimeType();
        this.url = command.url();
        this.sizeBytes = command.sizeBytes();
        this.createdAt = LocalDateTime.now();
    }

    // ========== Domain Methods ==========

    public Attachment updateFromCommand(UpdateAttachmentCommand command) {
        if (command.fileName() != null) {
            this.fileName = command.fileName();
        }
        if (command.mimeType() != null) {
            this.mimeType = command.mimeType();
        }
        if (command.url() != null) {
            this.url = command.url();
        }
        if (command.sizeBytes() != null) {
            this.sizeBytes = command.sizeBytes();
        }
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
