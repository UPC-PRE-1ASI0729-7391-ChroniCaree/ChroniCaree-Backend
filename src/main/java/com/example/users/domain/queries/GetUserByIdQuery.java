/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.users.domain.queries;

public record GetUserByIdQuery(Long id) {

    public GetUserByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id invalid");
        }
    }
}
