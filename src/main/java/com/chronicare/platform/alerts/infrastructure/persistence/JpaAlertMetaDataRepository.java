/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.alerts.infrastructure.persistence;

import com.chronicare.platform.alerts.domain.model.AlertMetaData;
import com.chronicare.platform.alerts.domain.repository.AlertMetaDataRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAlertMetaDataRepository extends JpaRepository<AlertMetaData, Long>, AlertMetaDataRepository {

    @Override
    List<AlertMetaData> findByVitalSign(String vitalSign);
}
