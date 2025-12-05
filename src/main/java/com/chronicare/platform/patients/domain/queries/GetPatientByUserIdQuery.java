package com.chronicare.platform.patients.domain.queries;

public record GetPatientByUserIdQuery(Long userId) {
    public GetPatientByUserIdQuery {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
    }
}
