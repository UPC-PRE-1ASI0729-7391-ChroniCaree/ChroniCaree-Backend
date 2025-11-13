/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.threads.domain.model;

import com.example.messages.domain.model.Message;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Entity
@Table(name = "threads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Thread {

    @Id
    private String id;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String doctorId;

    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String lastMessageAt;
    private String createdAt;
    private String updatedAt;

    @OneToMany(mappedBy = "threadId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;

    private Boolean hasUrgentMessages;
    private Integer unreadCount;
}
