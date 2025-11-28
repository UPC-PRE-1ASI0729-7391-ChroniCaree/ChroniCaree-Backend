package com.chronicare.platform.patients.domain.model.aggregates;

import com.chronicare.platform.patients.domain.commands.CreatePatientCommand;
import com.chronicare.platform.patients.domain.commands.UpdatePatientCommand;
import com.chronicare.platform.patients.domain.valueobjects.Dni;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Patient extends AuditableAbstractAggregateRoot<Patient> {

    @Embedded
    private Dni dni;

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String address;
    private String photoUrl;
    private Double weight;
    private Double height;

    public Patient(CreatePatientCommand command) {
        this.userId = command.userId();
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.email = command.email();
        this.dni = command.dni();
        this.birthDate = LocalDate.parse(command.birthDate());
        this.gender = command.gender();
        this.phone = command.phone();
        this.address = command.address();
        this.photoUrl = command.photoUrl();
        this.weight = command.weight();
        this.height = command.height();
    }

    public void update(UpdatePatientCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.email = command.email();
        this.dni = command.dni();
        this.birthDate = LocalDate.parse(command.birthDate());
        this.gender = command.gender();
        this.phone = command.phone();
        this.address = command.address();
        this.photoUrl = command.photoUrl();
        this.weight = command.weight();
        this.height = command.height();
    }
}
