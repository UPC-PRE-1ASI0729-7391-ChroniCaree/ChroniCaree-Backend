package com.chronicare.platform.messages.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Attachment {
    private String id;
    private String filename;
    private String mimeType;
    private Long size;
    private String downloadUrl;

    public Attachment() {}

    public Attachment(String id, String filename, String mimeType, Long size, String downloadUrl) {
        this.id = Objects.requireNonNull(id, "Attachment id is required");
        this.filename = Objects.requireNonNull(filename, "Attachment filename is required");
        this.mimeType = Objects.requireNonNull(mimeType, "Attachment mimeType is required");
        this.size = Objects.requireNonNull(size, "Attachment size is required");
        this.downloadUrl = Objects.requireNonNull(downloadUrl, "Attachment downloadUrl is required");
    }

    public String getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getSize() {
        return size;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
