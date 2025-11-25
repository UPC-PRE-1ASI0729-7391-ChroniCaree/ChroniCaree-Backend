package com.chronicare.platform.symptoms.domain.queries;

/**
 *
 * @author Barturen
 */
public record GetSymptomById(Long id) {

    public GetSymptomById {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id invalid");
        }
    }
}
