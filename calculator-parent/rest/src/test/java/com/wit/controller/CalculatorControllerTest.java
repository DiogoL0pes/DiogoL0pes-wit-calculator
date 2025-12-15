package com.wit.controller;

import com.wit.common.dto.CalculatorRequest;
import com.wit.common.dto.CalculatorResponse;
import com.wit.kafka.CalculatorKafkaClient;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
	    controllers = CalculatorController.class,
	    excludeFilters = {
	        @ComponentScan.Filter(
	            type = FilterType.ASSIGNABLE_TYPE,
	            classes = com.wit.kafka.KafkaConfig.class
	        )
	    }
	)
	class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculatorKafkaClient kafkaClient;

    @Test
    void sum_shouldReturnResult() throws Exception {
        Mockito.when(kafkaClient.sendAndReceive(Mockito.any(CalculatorRequest.class)))
                .thenReturn(new CalculatorResponse(new BigDecimal("3")));

        mockMvc.perform(get("/calculator/api/sum")
                        .param("a", "1")
                        .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(3));
    }

    @Test
    void division_shouldReturnDecimal() throws Exception {
        Mockito.when(kafkaClient.sendAndReceive(Mockito.any(CalculatorRequest.class)))
                .thenReturn(new CalculatorResponse(new BigDecimal("2.5")));

        mockMvc.perform(get("/calculator/api/division")
                        .param("a", "10")
                        .param("b", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(2.5));
    }
}

