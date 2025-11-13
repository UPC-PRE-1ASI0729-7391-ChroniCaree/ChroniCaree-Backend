package com.example.doctors.application.services;

import com.example.doctors.domain.model.Doctor;
import com.example.doctors.domain.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository repository;

    public DoctorService(DoctorRepository repository) {
        this.repository = repository;
    }

    public List<Doctor> getAll() {
        return repository.findAll();
    }

    public Optional<Doctor> getById(Long id) {
        return repository.findById(id);
    }

    public Doctor create(Doctor doctor) {
        return repository.save(doctor);
    }

    public Doctor update(Long id, Doctor doctorDetails) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setUser(doctorDetails.getUser());
                    existing.setTenant(doctorDetails.getTenant());
                    existing.setIndependent(doctorDetails.isIndependent());
                    existing.setFirstName(doctorDetails.getFirstName());
                    existing.setLastName(doctorDetails.getLastName());
                    existing.setDni(doctorDetails.getDni());
                    existing.setSpecialty(doctorDetails.getSpecialty());
                    existing.setLicenseNumber(doctorDetails.getLicenseNumber());
                    existing.setPhone(doctorDetails.getPhone());
                    existing.setVerified(doctorDetails.isVerified());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
