package com.chronicare.platform.messages.domain.model.valueobjects;

public enum ContentType {
    TEXT("TEXT", "Plain text"),
    HTML("HTML", "HTML content"),
    MARKDOWN("MARKDOWN", "Markdown content");

    private final String code;
    private final String description;

    ContentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ContentType fromCode(String code) {
        if (code == null) return ContentType.TEXT;
        for (ContentType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return ContentType.TEXT;
    }
}
