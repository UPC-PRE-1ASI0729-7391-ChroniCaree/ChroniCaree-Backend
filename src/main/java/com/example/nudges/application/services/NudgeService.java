/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.nudges.application.services;

import com.example.nudges.domain.model.Nudge;
import com.example.nudges.domain.repository.NudgeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NudgeService {

    private final NudgeRepository nudgeRepository;

    public NudgeService(NudgeRepository nudgeRepository) {
        this.nudgeRepository = nudgeRepository;
    }

    public List<Nudge> getAllNudges() {
        return nudgeRepository.findAll();
    }

    public Optional<Nudge> getNudgeById(Long id) {
        return nudgeRepository.findById(id);
    }

    public List<Nudge> getNudgesByPatientId(Long patientId) {
        return nudgeRepository.findByPatientId(patientId);
    }

    public Nudge createNudge(Nudge nudge) {
        return nudgeRepository.save(nudge);
    }

    public Nudge updateNudge(Long id, Nudge updatedNudge) {
        updatedNudge.setId(id);
        return nudgeRepository.save(updatedNudge);

    }

    public void deleteNudge(Long id) {
        nudgeRepository.deleteById(id);
    }
}
