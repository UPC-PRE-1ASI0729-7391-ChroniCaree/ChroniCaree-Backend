/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patients.domain.queries;

/**
 * Query to get a patient by id
 *
 * @param patientId The ID of the patient to retrieve
 */
public record GetPatientByIdQuery(Long patientId) {

}
