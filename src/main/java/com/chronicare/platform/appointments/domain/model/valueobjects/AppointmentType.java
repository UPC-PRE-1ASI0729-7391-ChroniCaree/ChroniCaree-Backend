package com.chronicare.platform.appointments.domain.model.valueobjects;

public enum AppointmentType {
    IN_PERSON("IN_PERSON"),
    TELEMEDICINE("TELEMEDICINE");

    private final String code;

    AppointmentType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
