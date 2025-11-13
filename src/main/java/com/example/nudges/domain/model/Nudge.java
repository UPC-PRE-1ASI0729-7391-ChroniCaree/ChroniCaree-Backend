/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.nudges.domain.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "nudges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nudge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NudgeType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NudgePriority priority;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    private String actionLabel;
    private String actionRoute;

    @Column(nullable = false)
    private String icon;

    @Column(nullable = false)
    private Boolean isDismissed;

    @Column(nullable = false)
    private Boolean isSnoozed;

    private String snoozeUntil;

    @Column(nullable = false)
    private String createdAt;

    private String dismissedAt;
}
