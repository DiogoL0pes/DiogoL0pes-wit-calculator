package com.wit.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

import com.wit.common.dto.CalculatorRequest;
import com.wit.common.dto.CalculatorResponse;
import com.wit.config.RequestIdFilter;

import java.util.concurrent.ExecutionException;

@Service
public class CalculatorKafkaClient {

    private final ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorResponse> template;

    public CalculatorKafkaClient(
            ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorResponse> template) {
        this.template = template;
    }

    public CalculatorResponse sendAndReceive(CalculatorRequest calculatorRequest)
            throws InterruptedException, ExecutionException {

        ProducerRecord<String, CalculatorRequest> record =
                new ProducerRecord<>("calculator-requests", calculatorRequest);

        // ✅ Usa SEMPRE o requestId que veio do controller
        record.headers().add(
                RequestIdFilter.REQUEST_ID_HEADER,
                calculatorRequest.getRequestId().getBytes()
        );

        var future = template.sendAndReceive(record);
        var consumerRecord = future.get();

        return consumerRecord.value();
    }
}

