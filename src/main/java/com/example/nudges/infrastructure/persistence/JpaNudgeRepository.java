/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.nudges.infrastructure.persistence;

import com.example.nudges.domain.model.Nudge;
import com.example.nudges.domain.repository.NudgeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaNudgeRepository extends JpaRepository<Nudge, Long>, NudgeRepository {

    @Override
    List<Nudge> findByPatientId(Long patientId);
}
