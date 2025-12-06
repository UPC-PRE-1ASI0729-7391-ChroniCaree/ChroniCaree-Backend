package com.chronicare.platform.symptoms.interfaces.rest;

import com.chronicare.platform.symptoms.application.services.SymptomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

@WebMvcTest(SymptomController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class SymptomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SymptomService service;

    @Test
    void getAllSymptoms_withInvalidPatientId_shouldReturnBadRequest() throws Exception {
        when(service.getSymptomsByPatientId(org.mockito.ArgumentMatchers.anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/symptoms").param("patientId", "not-a-number"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllSymptoms_withPatientId_shouldReturnEmptyArray() throws Exception {
        when(service.getSymptomsByPatientId(org.mockito.ArgumentMatchers.anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/symptoms").param("patientId", "123"))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().json("[]"));
    }
}
