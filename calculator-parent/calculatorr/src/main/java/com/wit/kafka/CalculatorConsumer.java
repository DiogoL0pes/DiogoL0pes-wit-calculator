package com.wit.kafka;



import java.math.BigDecimal;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.wit.common.Constants;
import com.wit.common.dto.CalculatorRequest;
import com.wit.common.dto.CalculatorResponse;
import com.wit.service.CalculatorService;
@Service
public class CalculatorConsumer {

    private final CalculatorService service;

    public CalculatorConsumer(CalculatorService service) {
        this.service = service;
    }

    @KafkaListener(
            topics = Constants.REQUEST_TOPIC,
            groupId = "calculator-group"
    )
    @SendTo   
    public CalculatorResponse consume(
            CalculatorRequest request,
            @Header(name = "X-Request-Id", required = false) String requestId
    ) {
        BigDecimal result =
                service.compute(request.getA(), request.getB(), request.getOperation());

        return new CalculatorResponse(result,request.getRequestId());
    }
}
