package com.chronicare.platform.medicalRecords.domain.services;

import com.chronicare.platform.medicalRecords.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.medicalRecords.domain.model.commands.CreateMedicalRecordCommand;
import com.chronicare.platform.medicalRecords.domain.model.commands.DeleteMedicalRecordCommand;
import com.chronicare.platform.medicalRecords.domain.model.commands.UpdateMedicalRecordCommand;

public interface MedicalRecordCommandService {
    MedicalRecord handle(CreateMedicalRecordCommand command);
    MedicalRecord handle(UpdateMedicalRecordCommand command);
    void handle(DeleteMedicalRecordCommand command);
}

