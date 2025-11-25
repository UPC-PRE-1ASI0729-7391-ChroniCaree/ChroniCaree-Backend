package com.chronicare.platform.patients.domain.model.aggregates;

import com.chronicare.platform.patients.domain.commands.CreatePatientCommand;
import com.chronicare.platform.patients.domain.commands.UpdatePatientCommand;
import com.chronicare.platform.patients.domain.valueobjects.Dni;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Patient extends AuditableAbstractAggregateRoot<Patient> {

    @Embedded
    private Dni dni;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String address;
    private Double weight;
    private Double height;

    public Patient() {
    }

    public Patient(CreatePatientCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.dni = command.dni();
        this.birthDate = LocalDate.parse(command.birthDate());
        this.gender = command.gender();
        this.phone = command.phone();
        this.address = command.address();
        this.weight = command.weight();
        this.height = command.height();
    }

    public void update(UpdatePatientCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.dni = command.dni();
        this.birthDate = LocalDate.parse(command.birthDate());
        this.gender = command.gender();
        this.phone = command.phone();
        this.address = command.address();
        this.weight = command.weight();
        this.height = command.height();
    }
}
