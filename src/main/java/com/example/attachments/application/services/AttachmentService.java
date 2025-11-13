/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.attachments.application.services;

import com.example.attachments.domain.model.Attachment;
import com.example.attachments.domain.repository.AttachmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAll();
    }

    public Optional<Attachment> getAttachmentById(String id) {
        return attachmentRepository.findById(id);
    }

    public Attachment createAttachment(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    public Attachment updateAttachment(String id, Attachment updatedAttachment) {
        return attachmentRepository.findById(id)
                .map(existing -> {
                    existing.setFileName(updatedAttachment.getFileName());
                    existing.setMimeType(updatedAttachment.getMimeType());
                    existing.setUrl(updatedAttachment.getUrl());
                    existing.setSizeBytes(updatedAttachment.getSizeBytes());
                    return attachmentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + id));
    }

    public void deleteAttachment(String id) {
        attachmentRepository.deleteById(id);
    }
}