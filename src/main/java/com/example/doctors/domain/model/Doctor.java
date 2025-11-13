package com.example.doctors.domain.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa a un médico dentro del sistema.
 */
@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = true)
    private Long tenantId;

    @Column(nullable = false)
    private boolean isIndependent;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 20)
    private String dni;

    @Column(nullable = false, length = 100)
    private String specialty;

    @Column(nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private boolean isVerified;
}
