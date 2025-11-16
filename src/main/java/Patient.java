
import com.example.patients.domain.commands.CreatePatientCommand;
import com.example.patients.domain.valueobjects.Dni;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Aggregate root for Patient
 */
@Getter
@Entity
@Table(name = "patients")
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Embedded
    @Column(nullable = false, unique = true)
    private Dni dni;

    @Column(nullable = false)
    private String birthDate;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double bmi;

    /**
     * Aggregate constructor from command
     *
     * @param command The command containing patient information
     */
    public Patient(CreatePatientCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.dni = command.dni(); // si command.dni() ya devuelve un Dni
        this.birthDate = command.birthDate();
        this.gender = command.gender();
        this.phone = command.phone();
        this.address = command.address();
        this.weight = command.weight();
        this.height = command.height();
        this.bmi = weight / (height * height);
    }

    /**
     * Update patient information
     *
     * @param updated The patient with updated information
     */
    public void updateFrom(Patient updated) {
        this.firstName = updated.firstName;
        this.lastName = updated.lastName;
        this.birthDate = updated.birthDate;
        this.gender = updated.gender;
        this.phone = updated.phone;
        this.address = updated.address;
        this.weight = updated.weight;
        this.height = updated.height;
        this.bmi = weight / (height * height);
    }
}
