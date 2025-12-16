package com.wit.controller;

import com.wit.common.Constants;
import com.wit.common.dto.CalculatorRequest;
import com.wit.common.dto.CalculatorResponse;
import com.wit.kafka.CalculatorKafkaClient;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/calculator/api")
public class CalculatorController {

    private final CalculatorKafkaClient kafkaClient;

    public CalculatorController(CalculatorKafkaClient kafkaClient) {
        this.kafkaClient = kafkaClient;
    }

    private String resolveRequestId(HttpServletRequest request) {
        String header = request.getHeader("X-Request-Id");
        return (header != null && !header.isBlank())
                ? header
                : UUID.randomUUID().toString();
    }

    
    @GetMapping("/sum")
    public ResponseEntity<Map<String, Object>> sum(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b,
            HttpServletRequest request) throws Exception {

    	String requestId = resolveRequestId(request);

        CalculatorRequest kafkaRequest = new CalculatorRequest();
        kafkaRequest.setA(a);
        kafkaRequest.setB(b);
        kafkaRequest.setOperation(Constants.SUM);
        kafkaRequest.setRequestId(requestId);

        CalculatorResponse response =
                kafkaClient.sendAndReceive(kafkaRequest);

        return ResponseEntity.ok()
                .header("X-Request-Id", requestId)
                .body(Map.of("result", response.getResult()));
    }
    
    @GetMapping("/subtraction")
    public ResponseEntity<Map<String, Object>> subtraction(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b,
            HttpServletRequest request) throws Exception {

    	String requestId = resolveRequestId(request);

        CalculatorRequest kafkaRequest = new CalculatorRequest();
        kafkaRequest.setA(a);
        kafkaRequest.setB(b);
        kafkaRequest.setOperation(Constants.SUB);
        kafkaRequest.setRequestId(requestId);

        CalculatorResponse response =
                kafkaClient.sendAndReceive(kafkaRequest);

        return ResponseEntity.ok()
                .header("X-Request-Id", requestId)
                .body(Map.of("result", response.getResult()));
    }
    
    @GetMapping("/multiplication")
    public ResponseEntity<Map<String, Object>> multiplication(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b,
            HttpServletRequest request) throws Exception {


    	String requestId = resolveRequestId(request);

        CalculatorRequest kafkaRequest = new CalculatorRequest();
        kafkaRequest.setA(a);
        kafkaRequest.setB(b);
        kafkaRequest.setOperation(Constants.MUL);
        kafkaRequest.setRequestId(requestId);

        CalculatorResponse response =
                kafkaClient.sendAndReceive(kafkaRequest);

        return ResponseEntity.ok()
                .header("X-Request-Id", requestId)
                .body(Map.of("result", response.getResult()));
    }
    
    @GetMapping("/division")
    public ResponseEntity<Map<String, Object>> division(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b,
            HttpServletRequest request) throws Exception {


    	String requestId = resolveRequestId(request);

        CalculatorRequest kafkaRequest = new CalculatorRequest();
        kafkaRequest.setA(a);
        kafkaRequest.setB(b);
        kafkaRequest.setOperation(Constants.DIV);
        kafkaRequest.setRequestId(requestId);

        CalculatorResponse response =
                kafkaClient.sendAndReceive(kafkaRequest);

        return ResponseEntity.ok()
                .header("X-Request-Id", requestId)
                .body(Map.of("result", response.getResult()));
    }

}