package com.chronicare.platform.doctors.application.internal.commandservices;

import com.chronicare.platform.doctors.domain.model.aggregates.Doctor;
import com.chronicare.platform.doctors.domain.model.commands.*;
import com.chronicare.platform.doctors.domain.model.valueobjects.*;
import com.chronicare.platform.doctors.domain.services.DoctorCommandService;
import com.chronicare.platform.doctors.infrastructure.persistence.jpa.repositories.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorCommandServiceImpl implements DoctorCommandService {

    private final DoctorRepository doctorRepository;

    public DoctorCommandServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Optional<Doctor> handle(CreateDoctorCommand command) {
        // Check if DNI already exists
        if (doctorRepository.findByDni_Value(command.dni()).isPresent()) {
            throw new IllegalArgumentException("Doctor with DNI " + command.dni() + " already exists");
        }

        // Check if license number already exists
        if (doctorRepository.findByLicenseNumber_Value(command.licenseNumber()).isPresent()) {
            throw new IllegalArgumentException("Doctor with license number " + command.licenseNumber() + " already exists");
        }

        // Check if user already has a doctor profile
        if (doctorRepository.findByUserId(command.userId()).isPresent()) {
            throw new IllegalArgumentException("User already has a doctor profile");
        }

        var name = new PersonName(command.firstName(), command.lastName());
        var dni = new DNI(command.dni());
        var specialty = new Specialty(command.specialty());
        var licenseNumber = new LicenseNumber(command.licenseNumber());
        var phone = new PhoneNumber(command.phone());

        var education = command.education() != null
            ? command.education().stream()
                .map(e -> new Education(e.degree(), e.institution(), e.year()))
                .collect(Collectors.toList())
            : new ArrayList<Education>();

        var doctor = new Doctor(
            command.userId(),
            command.tenantId(),
            name,
            dni,
            specialty,
            licenseNumber,
            phone,
            command.consultationFee(),
            command.languages(),
            education
        );

        var savedDoctor = doctorRepository.save(doctor);
        return Optional.of(savedDoctor);
    }

    @Override
    public Optional<Doctor> handle(UpdateDoctorCommand command) {
        var doctor = doctorRepository.findById(command.doctorId())
            .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        var name = new PersonName(command.firstName(), command.lastName());
        var phone = new PhoneNumber(command.phone());
        var specialty = new Specialty(command.specialty());

        doctor.updateProfile(name, phone, specialty, command.consultationFee(), command.languages());

        var updatedDoctor = doctorRepository.save(doctor);
        return Optional.of(updatedDoctor);
    }

    @Override
    public Optional<Doctor> handle(VerifyDoctorCommand command) {
        var doctor = doctorRepository.findById(command.doctorId())
            .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        doctor.verify();

        var verifiedDoctor = doctorRepository.save(doctor);
        return Optional.of(verifiedDoctor);
    }

    @Override
    public Optional<Doctor> handle(UpdateAcceptingPatientsCommand command) {
        var doctor = doctorRepository.findById(command.doctorId())
            .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        if (command.acceptingPatients()) {
            doctor.enablePatientAcceptance();
        } else {
            doctor.disablePatientAcceptance();
        }

        var updatedDoctor = doctorRepository.save(doctor);
        return Optional.of(updatedDoctor);
    }

    @Override
    public void handle(DeleteDoctorCommand command) {
        if (!doctorRepository.existsById(command.doctorId())) {
            throw new IllegalArgumentException("Doctor not found");
        }
        doctorRepository.deleteById(command.doctorId());
    }
}
