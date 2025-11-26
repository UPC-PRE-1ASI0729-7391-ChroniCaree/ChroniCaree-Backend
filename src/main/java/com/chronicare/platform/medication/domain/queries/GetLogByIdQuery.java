/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.domain.queries;

/**
 *
 * @author Barturen
 */
public record GetLogByIdQuery(Long logId) {

    public GetLogByIdQuery {
        if (logId == null || logId <= 0) {
            throw new IllegalArgumentException("logId cannot be null or negative");
        }
    }
}
