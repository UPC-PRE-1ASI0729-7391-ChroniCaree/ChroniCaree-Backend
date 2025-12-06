package com.chronicare.platform.records.domain.model.aggregates;

import com.chronicare.platform.records.domain.model.valueobjects.*;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "MedicalRecordAggregate")
@Table(name = "records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord extends AuditableAbstractAggregateRoot<MedicalRecord> {

    @Column(nullable = false, name = "tenant_id")
    private Long tenantId;

    @Column(nullable = false, name = "patient_id")
    private Long patientId;

    @Column(nullable = false, name = "author_id")
    private Long authorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType type;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT", name = "structured_data")
    private String structuredData; // JSON stored as text

    @ElementCollection
    @CollectionTable(name = "medical_record_attachments", joinColumns = @JoinColumn(name = "record_id"))
    @Column(name = "attachment_id")
    private List<String> attachments = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "medical_record_tags", joinColumns = @JoinColumn(name = "record_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordVisibility visibility;

    @Column(nullable = false)
    @Builder.Default
    private Integer version = 1;

    @Column(name = "parent_record_id")
    private Long parentRecordId; // For versioning

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deletion_note")
    private String deletionNote;

    // Business methods
    public void softDelete(String note) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletionNote = note;
    }

    public void addAttachment(String attachmentId) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(attachmentId);
    }

    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    public boolean isAccessibleBy(Long userId, String role) {
        // Basic access control logic
        if ("TENANT_ADMIN".equals(role)) {
            return true;
        }
        if (this.visibility == RecordVisibility.PRIVATE && !this.authorId.equals(userId)) {
            return false;
        }
        return true;
    }

    public MedicalRecord createNewVersion(String newContent, String structuredData, Long editorId) {
        return MedicalRecord.builder()
                .tenantId(this.tenantId)
                .patientId(this.patientId)
                .authorId(editorId)
                .type(this.type)
                .title(this.title)
                .content(newContent)
                .structuredData(structuredData)
                .attachments(new ArrayList<>(this.attachments))
                .tags(new ArrayList<>(this.tags))
                .visibility(this.visibility)
                .version(this.version + 1)
                .parentRecordId(this.getId())
                .isDeleted(false)
                .build();
    }
}
