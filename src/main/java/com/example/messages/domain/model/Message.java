/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.messages.domain.model;

import com.example.attachments.domain.model.Attachment;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    private Long threadId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderRole senderRole;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    private String subject;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments;

    private LocalDateTime createdAt;

    private boolean archivedToMedicalRecord;
    private boolean isUrgent;
    private boolean isRead;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
        this.archivedToMedicalRecord = false;
    }
}
