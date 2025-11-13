/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.attachments.domain.repository;

import com.example.attachments.domain.model.Attachment;
import java.util.List;
import java.util.Optional;

public interface AttachmentRepository {

    List<Attachment> findAll();

    Optional<Attachment> findById(String id);

    Attachment save(Attachment attachment);

    void deleteById(String id);
}
