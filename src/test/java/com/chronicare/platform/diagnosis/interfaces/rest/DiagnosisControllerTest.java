package com.chronicare.platform.diagnosis.interfaces.rest;

import com.chronicare.platform.diagnosis.domain.services.DiagnosisCommandService;
import com.chronicare.platform.diagnosis.domain.services.DiagnosisQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

@WebMvcTest(DiagnosisController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class DiagnosisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiagnosisQueryService diagnosisQueryService;

    @MockBean
    private DiagnosisCommandService diagnosisCommandService;

    @Test
    void getAllDiagnoses_withInvalidPatientId_shouldReturnBadRequest() throws Exception {
        when(diagnosisQueryService.handle(org.mockito.ArgumentMatchers.any(com.chronicare.platform.diagnosis.domain.model.queries.GetDiagnosesByPatientIdQuery.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/diagnoses").param("patientId", "abc123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllDiagnoses_withPatientId_shouldReturnEmptyArray() throws Exception {
        when(diagnosisQueryService.handle(org.mockito.ArgumentMatchers.any(com.chronicare.platform.diagnosis.domain.model.queries.GetDiagnosesByPatientIdQuery.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/diagnoses").param("patientId", "123"))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().json("[]"));
    }
}
