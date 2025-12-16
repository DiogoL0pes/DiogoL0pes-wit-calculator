package com.wit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.wit.common.dto.CalculatorResponse;
import com.wit.kafka.CalculatorKafkaClient;

@WebMvcTest(
	    controllers = CalculatorController.class,
	    excludeAutoConfiguration = {
	        KafkaAutoConfiguration.class,
	        org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.class
	    }
	)
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    
    @MockBean
    private CalculatorKafkaClient kafkaClient;

    @Test
    void sumEndpointReturns200() throws Exception {
        CalculatorResponse response = new CalculatorResponse(new BigDecimal("5"));
        Mockito.when(kafkaClient.sendAndReceive(Mockito.any())).thenReturn(response);
        mockMvc.perform(get("/calculator/api/sum")
                .param("a", "2")
                .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("5"));
    }
    
    @Test
    void subtraction_shouldReturnCorrectResult() throws Exception {
        when(kafkaClient.sendAndReceive(any()))
                .thenReturn(new CalculatorResponse(new BigDecimal("6")));

        mockMvc.perform(get("/calculator/api/subtraction")
                        .param("a", "10")
                        .param("b", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(6));
    }

    @Test
    void multiplication_shouldReturnCorrectResult() throws Exception {
        when(kafkaClient.sendAndReceive(any()))
                .thenReturn(new CalculatorResponse(new BigDecimal("20")));

        mockMvc.perform(get("/calculator/api/multiplication")
                        .param("a", "4")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(20));
    }

    @Test
    void division_shouldReturnDecimalResult() throws Exception {
        when(kafkaClient.sendAndReceive(any()))
                .thenReturn(new CalculatorResponse(new BigDecimal("2.5")));

        mockMvc.perform(get("/calculator/api/division")
                        .param("a", "10")
                        .param("b", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(2.5));
    }


    @Test
    void sum_shouldReturn400_whenMissingParameters() throws Exception {
        mockMvc.perform(get("/calculator/api/sum")
                        .param("a", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void division_shouldReturn400_whenInvalidNumber() throws Exception {
        mockMvc.perform(get("/calculator/api/division")
                        .param("a", "abc")
                        .param("b", "2"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldGenerateRequestIdWhenMissing() throws Exception {

        when(kafkaClient.sendAndReceive(any()))
                .thenReturn(new CalculatorResponse(BigDecimal.TEN));

        var result = mockMvc.perform(get("/calculator/api/sum")
                        .param("a", "5")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-Id"))
                .andReturn();

        String requestId = result.getResponse().getHeader("X-Request-Id");

        assertThat(requestId).isNotBlank();
        assertThatCode(() -> UUID.fromString(requestId)).doesNotThrowAnyException();
    }

    /*@Test
    void shouldReuseExistingRequestId() throws Exception {

        String existingRequestId = UUID.randomUUID().toString();

        when(kafkaClient.sendAndReceive(any()))
                .thenReturn(new CalculatorResponse(BigDecimal.valueOf(8)));

        mockMvc.perform(get("/calculator/api/sum")
                        .param("a", "3")
                        .param("b", "5")
                        .header("X-Request-Id", existingRequestId))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Request-Id", existingRequestId));
    }*/


    @Test
    void shouldPassRequestIdToKafkaClient() throws Exception {

        String requestId = UUID.randomUUID().toString();

        when(kafkaClient.sendAndReceive(any()))
                .thenReturn(new CalculatorResponse(BigDecimal.valueOf(6)));

        mockMvc.perform(get("/calculator/api/sum")
                        .param("a", "2")
                        .param("b", "4")
                        .header("X-Request-Id", requestId))
                .andExpect(status().isOk());

        var captor = ArgumentCaptor.forClass(com.wit.common.dto.CalculatorRequest.class);

        verify(kafkaClient).sendAndReceive(captor.capture());

        assertThat(captor.getValue().getRequestId())
                .isEqualTo(requestId);
    }
}
