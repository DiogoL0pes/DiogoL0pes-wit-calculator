package com.wit.kafka;


import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import com.wit.common.Constants;
import com.wit.common.dto.CalculatorRequest;
import com.wit.common.dto.CalculatorResponse;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class CalculatorKafkaClient {

    private final ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorResponse> template;

    public CalculatorKafkaClient(
            ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorResponse> template) {
        this.template = template;
    }

    public CalculatorResponse sendAndReceive(CalculatorRequest request) throws Exception {

        ProducerRecord<String, CalculatorRequest> record =
                new ProducerRecord<>(Constants.REQUEST_TOPIC, request);

        RequestReplyFuture<String, CalculatorRequest, CalculatorResponse> future =
                template.sendAndReceive(record);

        return future.get(5, TimeUnit.SECONDS).value();
    }
}

