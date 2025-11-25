package com.chronicare.platform.patients.domain.queries;

/**
 * Query to get a patient by id
 *
 * @param patientId The ID of the patient to retrieve
 */
public record GetPatientByIdQuery(Long patientId) {

}
