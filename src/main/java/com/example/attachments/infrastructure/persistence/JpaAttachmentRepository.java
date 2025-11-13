/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.attachments.infrastructure.persistence;

import com.example.attachments.domain.model.Attachment;
import com.example.attachments.domain.repository.AttachmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAttachmentRepository extends JpaRepository<Attachment, String>, AttachmentRepository {
}

