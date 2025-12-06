package com.chronicare.platform.messages.domain.model.valueobjects;

public enum ConversationType {
    ONE_TO_ONE("ONE_TO_ONE", "One to one conversation"),
    GROUP("GROUP", "Group conversation"),
    SYSTEM("SYSTEM", "System message conversation");

    private final String code;
    private final String description;

    ConversationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ConversationType fromCode(String code) {
        if (code == null) return null;
        for (ConversationType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown conversation type: " + code);
    }
}
