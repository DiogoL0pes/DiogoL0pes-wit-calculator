package com.wit.kafka;


import com.wit.common.Constants;
import com.wit.common.dto.CalculatorRequest;
import com.wit.common.dto.CalculatorResponse;
import com.wit.service.CalculatorService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculatorConsumer {

    private final CalculatorService service;
    private final KafkaTemplate<String, CalculatorResponse> kafkaTemplate;

    public CalculatorConsumer(CalculatorService service,
                              KafkaTemplate<String, CalculatorResponse> kafkaTemplate) {
        this.service = service;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
    	    topics = Constants.REQUEST_TOPIC,
    	    groupId = "calculator-group"
    	)
    	@SendTo
    	public CalculatorResponse consume(CalculatorRequest request) {

    	    BigDecimal result =
    	            service.compute(request.getA(), request.getB(), request.getOperation());

    	    return new CalculatorResponse(result);
    	}
}
