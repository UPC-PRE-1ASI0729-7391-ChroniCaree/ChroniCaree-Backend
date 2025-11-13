/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.nudges.domain.repository;

import com.example.nudges.domain.model.Nudge;
import java.util.List;
import java.util.Optional;


public interface NudgeRepository {

    List<Nudge> findAll();

    Optional<Nudge> findById(Long id);

    List<Nudge> findByPatientId(Long patientId);

    Nudge save(Nudge nudge);

    void deleteById(Long id);
}
