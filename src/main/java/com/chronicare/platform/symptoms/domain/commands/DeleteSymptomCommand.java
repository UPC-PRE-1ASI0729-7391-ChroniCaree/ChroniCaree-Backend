package com.chronicare.platform.symptoms.domain.commands;

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
