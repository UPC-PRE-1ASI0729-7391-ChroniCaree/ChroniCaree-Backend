package com.chronicare.platform.messages.interfaces.rest.controller;

import com.chronicare.platform.messages.domain.model.valueobjects.Attachment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for Attachment management (Messages Bounded Context)
 */
@RestController
@RequestMapping("/api/v1/attachments")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT', 'ADMIN')")
public class MessageAttachmentController {

    @PostMapping("/upload")
    public ResponseEntity<AttachmentUploadResponse> uploadAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "messageId", required = false) String messageId) {
        
        try {
            String filename = file.getOriginalFilename();
            String mimeType = file.getContentType();
            long size = file.getSize();
            String downloadUrl = generateDownloadUrl(filename);
            
            AttachmentUploadResponse response = new AttachmentUploadResponse();
            response.setId(UUID.randomUUID().toString());
            response.setFilename(filename);
            response.setMimeType(mimeType);
            response.setSize(size);
            response.setDownloadUrl(downloadUrl);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrl(
            @RequestParam String filename,
            @RequestParam String mimeType) {
        
        PresignedUrlResponse response = new PresignedUrlResponse();
        response.setUploadUrl(generatePresignedUploadUrl(filename));
        response.setDownloadUrl(generateDownloadUrl(filename));
        response.setExpiresIn(3600);
        
        return ResponseEntity.ok(response);
    }

    private String generateDownloadUrl(String filename) {
        return "/api/v1/attachments/download/" + filename;
    }

    private String generatePresignedUploadUrl(String filename) {
        return "/api/v1/attachments/presigned-upload?filename=" + filename;
    }

    public static class AttachmentUploadResponse {
        private String id;
        private String filename;
        private String mimeType;
        private long size;
        private String downloadUrl;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getFilename() { return filename; }
        public void setFilename(String filename) { this.filename = filename; }
        public String getMimeType() { return mimeType; }
        public void setMimeType(String mimeType) { this.mimeType = mimeType; }
        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    }

    public static class PresignedUrlResponse {
        private String uploadUrl;
        private String downloadUrl;
        private int expiresIn;

        // Getters and Setters
        public String getUploadUrl() { return uploadUrl; }
        public void setUploadUrl(String uploadUrl) { this.uploadUrl = uploadUrl; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
        public int getExpiresIn() { return expiresIn; }
        public void setExpiresIn(int expiresIn) { this.expiresIn = expiresIn; }
    }
}
