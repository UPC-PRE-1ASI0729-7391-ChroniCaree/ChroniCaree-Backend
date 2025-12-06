package com.chronicare.platform.records.application.internal.commandservices;

import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.records.domain.model.entities.RecordAuditLog;
import com.chronicare.platform.records.domain.model.valueobjects.RecordType;
import com.chronicare.platform.records.domain.model.valueobjects.RecordVisibility;
import com.chronicare.platform.records.domain.services.RecordAccessService;
import com.chronicare.platform.records.domain.services.RecordVersioningService;
import com.chronicare.platform.records.infrastructure.persistence.jpa.repositories.MedicalRecordRepository;
import com.chronicare.platform.records.infrastructure.persistence.jpa.repositories.RecordAuditLogRepository;
import com.chronicare.platform.records.infrastructure.persistence.jpa.repositories.RecordConsentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MedicalRecordCommandService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final RecordAuditLogRepository auditLogRepository;
    private final RecordConsentRepository consentRepository;
    private final RecordAccessService accessService;
    private final RecordVersioningService versioningService;
    private final ObjectMapper objectMapper;

    @Transactional
    public MedicalRecord createRecord(CreateRecordCommand command) {
        // Create record
        MedicalRecord record = MedicalRecord.builder()
                .tenantId(command.tenantId())
                .patientId(command.patientId())
                .authorId(command.authorId())
                .type(command.type())
                .title(command.title())
                .content(command.content())
                .structuredData(command.structuredData())
                .attachments(command.attachments())
                .tags(command.tags())
                .visibility(command.visibility() != null ? command.visibility() : RecordVisibility.TEAM)
                .version(1)
                .isDeleted(false)
                .build();

        MedicalRecord savedRecord = medicalRecordRepository.save(record);

        // Create audit log
        createAuditLog(savedRecord.getId(), command.authorId(), "CREATED", 
                "Record created", command.ipAddress(), command.userAgent());

        return savedRecord;
    }

    @Transactional
    public MedicalRecord updateRecord(Long recordId, UpdateRecordCommand command) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + recordId));

        if (!accessService.canModify(record, command.userId(), command.userRole())) {
            throw new SecurityException("Access denied");
        }

        // Update fields
        if (command.title() != null) {
            record.setTitle(command.title());
        }
        if (command.content() != null) {
            record.setContent(command.content());
        }
        if (command.structuredData() != null) {
            record.setStructuredData(command.structuredData());
        }
        if (command.visibility() != null) {
            record.setVisibility(command.visibility());
        }

        MedicalRecord updated = medicalRecordRepository.save(record);

        // Audit
        Map<String, Object> changes = new HashMap<>();
        changes.put("recordId", recordId);
        changes.put("changes", command);
        createAuditLog(recordId, command.userId(), "UPDATED", 
                serializeToJson(changes), command.ipAddress(), command.userAgent());

        return updated;
    }

    @Transactional
    public MedicalRecord createVersion(Long recordId, CreateVersionCommand command) {
        MedicalRecord baseRecord = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + recordId));

        if (!accessService.canModify(baseRecord, command.editorId(), command.userRole())) {
            throw new SecurityException("Access denied");
        }

        // Create new version
        MedicalRecord newVersion = versioningService.createVersion(
                baseRecord,
                command.content(),
                command.structuredData(),
                command.editorId(),
                command.versionNote()
        );

        MedicalRecord saved = medicalRecordRepository.save(newVersion);

        // Audit
        Map<String, Object> details = new HashMap<>();
        details.put("baseRecordId", recordId);
        details.put("newVersion", saved.getVersion());
        details.put("note", command.versionNote());
        createAuditLog(saved.getId(), command.editorId(), "VERSION_CREATED",
                serializeToJson(details), command.ipAddress(), command.userAgent());

        return saved;
    }

    @Transactional
    public void softDeleteRecord(Long recordId, DeleteRecordCommand command) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + recordId));

        if (!accessService.canDelete(record, command.userId(), command.userRole())) {
            throw new SecurityException("Access denied");
        }

        record.softDelete(command.deletionNote());
        medicalRecordRepository.save(record);

        // Audit
        createAuditLog(recordId, command.userId(), "DELETED",
                "Soft delete: " + command.deletionNote(), command.ipAddress(), command.userAgent());
    }

    @Transactional
    public void hardDeleteRecord(Long recordId, DeleteRecordCommand command) {
        if (!"TENANT_ADMIN".equals(command.userRole())) {
            throw new SecurityException("Only tenant admins can hard delete records");
        }

        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + recordId));

        // Audit before delete
        createAuditLog(recordId, command.userId(), "HARD_DELETED",
                "Hard delete: " + command.deletionNote(), command.ipAddress(), command.userAgent());

        medicalRecordRepository.delete(record);
    }

    @Transactional
    public void addAttachment(Long recordId, String attachmentId, Long userId, String userRole) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + recordId));

        if (!accessService.canModify(record, userId, userRole)) {
            throw new SecurityException("Access denied");
        }

        record.addAttachment(attachmentId);
        medicalRecordRepository.save(record);

        createAuditLog(recordId, userId, "ATTACHMENT_ADDED",
                "Attachment added: " + attachmentId, null, null);
    }

    @Transactional
    public void addTag(Long recordId, String tag, Long userId, String userRole) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + recordId));

        if (!accessService.canModify(record, userId, userRole)) {
            throw new SecurityException("Access denied");
        }

        record.addTag(tag);
        medicalRecordRepository.save(record);

        createAuditLog(recordId, userId, "TAG_ADDED",
                "Tag added: " + tag, null, null);
    }

    private void createAuditLog(Long recordId, Long userId, String action, String details, 
                                String ipAddress, String userAgent) {
        RecordAuditLog log = RecordAuditLog.builder()
                .recordId(recordId)
                .userId(userId)
                .action(action)
                .details(details)
                .ipAddress(ipAddress != null ? ipAddress : "unknown")
                .userAgent(userAgent)
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }

    private String serializeToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }

    // Command records
    public record CreateRecordCommand(
            Long tenantId,
            Long patientId,
            Long authorId,
            RecordType type,
            String title,
            String content,
            String structuredData,
            List<String> attachments,
            List<String> tags,
            RecordVisibility visibility,
            String ipAddress,
            String userAgent
    ) {}

    public record UpdateRecordCommand(
            Long userId,
            String userRole,
            String title,
            String content,
            String structuredData,
            RecordVisibility visibility,
            String ipAddress,
            String userAgent
    ) {}

    public record CreateVersionCommand(
            Long editorId,
            String userRole,
            String content,
            String structuredData,
            String versionNote,
            String ipAddress,
            String userAgent
    ) {}

    public record DeleteRecordCommand(
            Long userId,
            String userRole,
            String deletionNote,
            String ipAddress,
            String userAgent
    ) {}
}
