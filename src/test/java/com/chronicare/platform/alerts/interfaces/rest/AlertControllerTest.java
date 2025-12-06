package com.chronicare.platform.alerts.interfaces.rest;

import com.chronicare.platform.alerts.domain.services.AlertCommandService;
import com.chronicare.platform.alerts.domain.services.AlertQueryService;
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

@WebMvcTest(AlertController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertQueryService alertQueryService;

    @MockBean
    private AlertCommandService alertCommandService;

    @Test
    void getAllAlerts_withPatientId_shouldReturnEmptyArray() throws Exception {
        when(alertQueryService.handle(org.mockito.ArgumentMatchers.any(com.chronicare.platform.alerts.domain.model.queries.GetAlertsByPatientIdQuery.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/alerts").param("patientId", "123"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
