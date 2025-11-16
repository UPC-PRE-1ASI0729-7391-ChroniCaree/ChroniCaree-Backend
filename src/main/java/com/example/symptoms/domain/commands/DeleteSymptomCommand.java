/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.symptoms.domain.commands;

/**
 *
 * @author Barturen
 */
public record DeleteSymptomCommand(Long symptomId) {

    public DeleteSymptomCommand {
        if (symptomId == null) {
            throw new IllegalArgumentException("symptomId invalid");

        }
    }

}
