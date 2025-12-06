package com.chronicare.platform.records.application.internal.eventhandlers;

import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordEventHandler {

    @EventListener
    public void handleRecordCreated(RecordCreatedEvent event) {
        log.info("Record created: id={}, patientId={}, type={}", 
                event.getRecordId(), event.getPatientId(), event.getType());
        
        // Publish to message queue or event bus
        // Integrate with search indexing service
        // Send notifications if needed
    }

    @EventListener
    public void handleRecordUpdated(RecordUpdatedEvent event) {
        log.info("Record updated: id={}, version={}", 
                event.getRecordId(), event.getVersion());
        
        // Update search index
        // Send notifications
    }

    @EventListener
    public void handleRecordDeleted(RecordDeletedEvent event) {
        log.info("Record deleted: id={}, soft={}", 
                event.getRecordId(), event.isSoftDelete());
        
        // Remove from search index
        // Notify relevant parties
    }

    @EventListener
    public void handleRecordViewed(RecordViewedEvent event) {
        log.debug("Record viewed: id={}, userId={}", 
                event.getRecordId(), event.getUserId());
        
        // Analytics tracking
    }

    // Event classes
    public static class RecordCreatedEvent {
        private final Long recordId;
        private final Long patientId;
        private final String type;

        public RecordCreatedEvent(Long recordId, Long patientId, String type) {
            this.recordId = recordId;
            this.patientId = patientId;
            this.type = type;
        }

        public Long getRecordId() { return recordId; }
        public Long getPatientId() { return patientId; }
        public String getType() { return type; }
    }

    public static class RecordUpdatedEvent {
        private final Long recordId;
        private final Integer version;

        public RecordUpdatedEvent(Long recordId, Integer version) {
            this.recordId = recordId;
            this.version = version;
        }

        public Long getRecordId() { return recordId; }
        public Integer getVersion() { return version; }
    }

    public static class RecordDeletedEvent {
        private final Long recordId;
        private final boolean softDelete;

        public RecordDeletedEvent(Long recordId, boolean softDelete) {
            this.recordId = recordId;
            this.softDelete = softDelete;
        }

        public Long getRecordId() { return recordId; }
        public boolean isSoftDelete() { return softDelete; }
    }

    public static class RecordViewedEvent {
        private final Long recordId;
        private final Long userId;

        public RecordViewedEvent(Long recordId, Long userId) {
            this.recordId = recordId;
            this.userId = userId;
        }

        public Long getRecordId() { return recordId; }
        public Long getUserId() { return userId; }
    }
}
