/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.symptoms.domain.queries;


 import com.example.symptoms.domain.valueobject.PatientId;

public record GetSymptomsByPatientQuery(PatientId patientId) { }