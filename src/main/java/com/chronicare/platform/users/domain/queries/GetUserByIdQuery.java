package com.chronicare.platform.users.domain.queries;

public record GetUserByIdQuery(Long id) {

    public GetUserByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id invalid");
        }
    }
}
