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
        return nudgeRepository.findById(id)
                .map(existing -> {
                    existing.setType(updatedNudge.getType());
                    existing.setPriority(updatedNudge.getPriority());
                    existing.setTitle(updatedNudge.getTitle());
                    existing.setMessage(updatedNudge.getMessage());
                    existing.setActionLabel(updatedNudge.getActionLabel());
                    existing.setActionRoute(updatedNudge.getActionRoute());
                    existing.setIcon(updatedNudge.getIcon());
                    existing.setDismissedAt(updatedNudge.getDismissedAt());
                    existing.setIsSnoozed(updatedNudge.getIsSnoozed());
                    existing.setSnoozeUntil(updatedNudge.getSnoozeUntil());
                    existing.setDismissedAt(updatedNudge.getDismissedAt());
                    return nudgeRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Nudge not found with id: " + id));
    }

    public void deleteNudge(Long id) {
        nudgeRepository.deleteById(id);
    }
}
