/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.alerts.domain.repository;

import com.example.alerts.domain.model.AlertMetaData;
import java.util.List;
import java.util.Optional;

public interface AlertMetaDataRepository {

    List<AlertMetaData> findAll();

    Optional<AlertMetaData> findById(Long id);

    List<AlertMetaData> findByVitalSign(String vitalSign);

    AlertMetaData save(AlertMetaData alertMetaData);

    void deleteById(Long id);
}
