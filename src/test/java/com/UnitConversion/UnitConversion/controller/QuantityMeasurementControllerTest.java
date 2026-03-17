package com.UnitConversion.UnitConversion.controller;

import com.UnitConversion.UnitConversion.config.SecurityConfig;
import com.UnitConversion.UnitConversion.model.OperationType;
import com.UnitConversion.UnitConversion.model.QuantityDTO;
import com.UnitConversion.UnitConversion.model.QuantityInputDTO;
import com.UnitConversion.UnitConversion.model.QuantityMeasurementDTO;
import com.UnitConversion.UnitConversion.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuantityMeasurementController.class)
@Import(SecurityConfig.class)
class QuantityMeasurementControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    IQuantityMeasurementService service;

    @Test
    void compare_ShouldReturn200() throws Exception {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit")
        );

        QuantityMeasurementDTO response = new QuantityMeasurementDTO();
        response.setOperation(OperationType.COMPARE);
        response.setResultString("true");

        Mockito.when(service.compare(Mockito.any(), Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("COMPARE"))
                .andExpect(jsonPath("$.resultString").value("true"));
    }

    @Test
    void historyByOperation_ShouldReturn200() throws Exception {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setOperation(OperationType.ADD);
        Mockito.when(service.getHistoryByOperation(OperationType.ADD)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/quantities/history/operation/ADD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operation").value("ADD"));
    }
}

