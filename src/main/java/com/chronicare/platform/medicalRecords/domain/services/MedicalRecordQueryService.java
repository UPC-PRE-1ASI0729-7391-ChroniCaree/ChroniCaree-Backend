package com.chronicare.platform.medicalRecords.domain.services;

import com.chronicare.platform.medicalRecords.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetAllMedicalRecordsQuery;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetMedicalRecordByIdQuery;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetMedicalRecordsByDoctorIdQuery;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetMedicalRecordsByPatientIdQuery;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordQueryService {
    List<MedicalRecord> handle(GetAllMedicalRecordsQuery query);
    Optional<MedicalRecord> handle(GetMedicalRecordByIdQuery query);
    List<MedicalRecord> handle(GetMedicalRecordsByPatientIdQuery query);
    List<MedicalRecord> handle(GetMedicalRecordsByDoctorIdQuery query);
}

