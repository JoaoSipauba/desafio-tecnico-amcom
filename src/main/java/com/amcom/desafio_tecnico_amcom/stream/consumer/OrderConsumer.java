package com.amcom.desafio_tecnico_amcom.stream.consumer;

import com.amcom.desafio_tecnico_amcom.command.OrderCommand;
import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.amcom.desafio_tecnico_amcom.support.constant.BrokerConstant.Consumer.GROUP_ID;
import static com.amcom.desafio_tecnico_amcom.support.constant.BrokerConstant.Topics.ORDER_EVENTS_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderConsumer {

    private final Map<String, OrderCommand> commandMap;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = ORDER_EVENTS_TOPIC, groupId = GROUP_ID)
    public void listen(String message) {
        try {
            log.info("Received message: {}", message);
            OrderEvent event = objectMapper.readValue(message, OrderEvent.class);

            OrderCommand command = commandMap.get(event.getEventType().toString());
            command.execute(event);

            log.info("Message processed and acknowledged: {}", message);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            throw new RuntimeException(e);
        }
    }
}