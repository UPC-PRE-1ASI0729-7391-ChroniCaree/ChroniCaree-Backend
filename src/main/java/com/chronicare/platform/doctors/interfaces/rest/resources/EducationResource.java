package com.chronicare.platform.doctors.interfaces.rest.resources;

public record EducationResource(
    String degree,
    String institution,
    Integer year
) {}
