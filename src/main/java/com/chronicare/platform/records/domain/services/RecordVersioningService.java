package com.chronicare.platform.records.domain.services;

import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordVersioningService {

    public MedicalRecord createVersion(MedicalRecord baseRecord, String newContent, String structuredData, Long editorId, String versionNote) {
        MedicalRecord newVersion = baseRecord.createNewVersion(newContent, structuredData, editorId);
        // Store version note in audit log (handled by service layer)
        return newVersion;
    }

    public List<MedicalRecord> getVersionHistory(List<MedicalRecord> allVersions) {
        // Sort by version number
        allVersions.sort((a, b) -> b.getVersion().compareTo(a.getVersion()));
        return allVersions;
    }

    public boolean isLatestVersion(MedicalRecord record, List<MedicalRecord> allVersions) {
        return allVersions.stream()
                .noneMatch(r -> r.getParentRecordId() != null && r.getParentRecordId().equals(record.getId()));
    }
}
