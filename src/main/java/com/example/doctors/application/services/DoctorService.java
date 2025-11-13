package com.example.doctors.application.services;

import com.example.doctors.domain.model.Doctor;
import com.example.doctors.domain.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para la gestión de médicos.
 */
@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        return doctorRepository.findById(id)
                .map(existing -> {
                    existing.setUserId(updatedDoctor.getUserId());
                    existing.setTenantId(updatedDoctor.getTenantId());
                    existing.setIndependent(updatedDoctor.isIndependent());
                    existing.setFirstName(updatedDoctor.getFirstName());
                    existing.setLastName(updatedDoctor.getLastName());
                    existing.setDni(updatedDoctor.getDni());
                    existing.setSpecialty(updatedDoctor.getSpecialty());
                    existing.setLicenseNumber(updatedDoctor.getLicenseNumber());
                    existing.setPhone(updatedDoctor.getPhone());
                    existing.setVerified(updatedDoctor.isVerified());
                    return doctorRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
