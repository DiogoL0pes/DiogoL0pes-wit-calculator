package com.wit.kafka;

import com.wit.common.Constants;
import com.wit.common.dto.CalculatorRequest;
import com.wit.common.dto.CalculatorResponse;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    // -------- PRODUCER (requests) --------
    @Bean
    public ProducerFactory<String, CalculatorRequest> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    // -------- CONSUMER (replies) --------
    @Bean
    public ConsumerFactory<String, CalculatorResponse> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "rest-replies");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(CalculatorResponse.class, false)
        );
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, CalculatorResponse> repliesContainer(
            ConsumerFactory<String, CalculatorResponse> cf) {

        ConcurrentKafkaListenerContainerFactory<String, CalculatorResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(cf);

        ConcurrentMessageListenerContainer<String, CalculatorResponse> container =
                factory.createContainer(Constants.RESPONSE_TOPIC);

        return container;
    }

    // -------- 🔥 ISTO É O BEAN QUE FALTA --------
    @Bean
    public ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorResponse>
    replyingKafkaTemplate(
            ProducerFactory<String, CalculatorRequest> pf,
            ConcurrentMessageListenerContainer<String, CalculatorResponse> repliesContainer) {

        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }
}