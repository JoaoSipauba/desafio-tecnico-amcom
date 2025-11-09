package com.amcom.desafio_tecnico_amcom.stream.producer;

import com.amcom.desafio_tecnico_amcom.model.Event.ProcessedOrderEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.amcom.desafio_tecnico_amcom.support.constant.BrokerConstant.Topics.ORDER_PROCESSED_TOPIC;

@Component
@RequiredArgsConstructor
public class ProcessedOrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendProcessedOrderEvent(ProcessedOrderEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(ORDER_PROCESSED_TOPIC, event.getExternalId(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ProcessedOrderEvent to JSON", e);
        }
    }
}
