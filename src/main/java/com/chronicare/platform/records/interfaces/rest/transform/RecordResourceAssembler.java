package com.chronicare.platform.records.interfaces.rest.transform;

import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.records.domain.model.entities.RecordAuditLog;
import com.chronicare.platform.records.interfaces.rest.resources.*;

public class RecordResourceAssembler {

    public static MedicalRecordResource toResourceFromEntity(MedicalRecord record) {
        return new MedicalRecordResource(
                record.getId(),
                record.getTenantId(),
                record.getPatientId(),
                record.getAuthorId(),
                record.getType(),
                record.getTitle(),
                record.getContent(),
                record.getStructuredData(),
                record.getAttachments(),
                record.getTags(),
                record.getVisibility(),
                record.getVersion(),
                record.getParentRecordId(),
                record.getIsDeleted(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }

    public static RecordAuditLogResource toResourceFromEntity(RecordAuditLog log) {
        return new RecordAuditLogResource(
                log.getId(),
                log.getRecordId(),
                log.getUserId(),
                log.getAction(),
                log.getDetails(),
                log.getIpAddress(),
                log.getTimestamp()
        );
    }
}
