package com.chronicare.platform.medication.interfaces.rest;

import com.chronicare.platform.medication.domain.services.MedicationCommandService;
import com.chronicare.platform.medication.domain.services.MedicationQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

@WebMvcTest(MedicationController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicationQueryService medicationQueryService;

    @MockBean
    private MedicationCommandService medicationCommandService;

    @Test
    void getAllMedications_withPatientId_shouldReturnEmptyArray() throws Exception {
        when(medicationQueryService.handle(org.mockito.ArgumentMatchers.any(com.chronicare.platform.medication.domain.model.queries.GetMedicationsByPatientIdQuery.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/medications").param("patientId", "PATIENT-123"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
