package com.chronicare.platform.symptoms.domain.queries;


 import com.chronicare.platform.symptoms.domain.valueobject.PatientId;

public record GetSymptomsByPatientQuery(PatientId patientId) { }
