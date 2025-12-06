package com.chronicare.platform.messages.domain.model.valueobjects;

public enum MessageStatus {
    QUEUED("QUEUED", "Message queued for sending"),
    SENT("SENT", "Message sent"),
    DELIVERED("DELIVERED", "Message delivered to recipient"),
    READ("READ", "Message read by recipient"),
    FAILED("FAILED", "Message delivery failed"),
    ARCHIVED("ARCHIVED", "Message archived");

    private final String code;
    private final String description;

    MessageStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MessageStatus fromCode(String code) {
        if (code == null) return null;
        for (MessageStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown message status: " + code);
    }
}
