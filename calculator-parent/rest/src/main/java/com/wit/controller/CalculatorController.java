package com.wit.controller;

import com.wit.common.Constants;
import com.wit.common.dto.CalculatorRequest;
import com.wit.common.dto.CalculatorResponse;
import com.wit.kafka.CalculatorKafkaClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/calculator/api")
public class CalculatorController {

    private final CalculatorKafkaClient kafkaClient;

    public CalculatorController(CalculatorKafkaClient kafkaClient) {
        this.kafkaClient = kafkaClient;
    }

    @GetMapping("/sum")
    public ResponseEntity<Map<String, Object>> sum(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b) throws Exception {

        CalculatorRequest request =
                new CalculatorRequest(a, b, Constants.SUM);

        CalculatorResponse response =
                kafkaClient.sendAndReceive(request);

        return ResponseEntity.ok(Map.of("result", response.getResult()));
    }
    
    @GetMapping("/subtraction")
    public ResponseEntity<Map<String, Object>> subtraction(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b) throws Exception {

        CalculatorRequest request =
                new CalculatorRequest(a, b, Constants.SUB);

        CalculatorResponse response =
                kafkaClient.sendAndReceive(request);

        return ResponseEntity.ok(Map.of("result", response.getResult()));
    }
    
    @GetMapping("/multiplication")
    public ResponseEntity<Map<String, Object>> multiplication(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b) throws Exception {

        CalculatorRequest request =
                new CalculatorRequest(a, b, Constants.MUL);

        CalculatorResponse response =
                kafkaClient.sendAndReceive(request);

        return ResponseEntity.ok(Map.of("result", response.getResult()));
    }
    
    @GetMapping("/division")
    public ResponseEntity<Map<String, Object>> division(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b) throws Exception {

        CalculatorRequest request =
                new CalculatorRequest(a, b, Constants.DIV);

        CalculatorResponse response =
                kafkaClient.sendAndReceive(request);

        return ResponseEntity.ok(Map.of("result", response.getResult()));
    }

}