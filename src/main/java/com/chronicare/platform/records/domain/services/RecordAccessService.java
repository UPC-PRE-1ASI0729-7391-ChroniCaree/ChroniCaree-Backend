package com.chronicare.platform.records.domain.services;

import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.records.domain.model.entities.RecordConsent;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecordAccessService {

    public boolean canAccess(MedicalRecord record, Long userId, String role, Optional<RecordConsent> consent) {
        // Tenant admin has full access
        if ("TENANT_ADMIN".equals(role)) {
            return true;
        }

        // Check if record is deleted
        if (record.getIsDeleted()) {
            return "TENANT_ADMIN".equals(role);
        }

        // Author always has access
        if (record.getAuthorId().equals(userId)) {
            return true;
        }

        // Check consent
        if (consent.isPresent() && consent.get().isValid()) {
            return true;
        }

        // Check visibility
        switch (record.getVisibility()) {
            case PRIVATE:
                return false;
            case TEAM:
                // TODO: Check if user is in care team
                return "DOCTOR".equals(role) || "NURSE".equals(role);
            case PUBLIC_WITHIN_TENANT:
                return true;
            default:
                return false;
        }
    }

    public boolean canModify(MedicalRecord record, Long userId, String role) {
        if ("TENANT_ADMIN".equals(role)) {
            return true;
        }

        if (record.getIsDeleted()) {
            return false;
        }

        return record.getAuthorId().equals(userId);
    }

    public boolean canDelete(MedicalRecord record, Long userId, String role) {
        if ("TENANT_ADMIN".equals(role)) {
            return true;
        }

        return record.getAuthorId().equals(userId);
    }
}
