package com.chronicare.platform.users.domain.queries;

public record GetUserByEmailQuery(String email) {

    public GetUserByEmailQuery {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email required");
        }
    }
}
