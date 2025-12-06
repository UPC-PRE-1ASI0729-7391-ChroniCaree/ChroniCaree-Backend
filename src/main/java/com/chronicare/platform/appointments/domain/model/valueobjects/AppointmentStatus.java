package com.chronicare.platform.appointments.domain.model.valueobjects;

public enum AppointmentStatus {
    SCHEDULED("SCHEDULED"),
    CONFIRMED("CONFIRMED"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED"),
    NO_SHOW("NO_SHOW"),
    RESCHEDULED("RESCHEDULED");

    private final String code;

    AppointmentStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
