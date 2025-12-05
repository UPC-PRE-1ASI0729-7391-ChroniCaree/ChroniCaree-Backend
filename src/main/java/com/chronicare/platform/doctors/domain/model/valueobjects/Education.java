package com.chronicare.platform.doctors.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Education {
    @Column(name = "degree", nullable = false)
    private String degree;

    @Column(name = "institution", nullable = false)
    private String institution;

    @Column(name = "year", nullable = false)
    private Integer year;

    protected Education() {}

    public Education(String degree, String institution, Integer year) {
        if (degree == null || degree.isBlank()) {
            throw new IllegalArgumentException("Degree cannot be empty");
        }
        if (institution == null || institution.isBlank()) {
            throw new IllegalArgumentException("Institution cannot be empty");
        }
        if (year == null || year < 1900 || year > 2100) {
            throw new IllegalArgumentException("Year must be valid");
        }
        this.degree = degree;
        this.institution = institution;
        this.year = year;
    }
}
