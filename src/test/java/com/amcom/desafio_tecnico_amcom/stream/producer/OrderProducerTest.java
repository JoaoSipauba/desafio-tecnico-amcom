package com.amcom.desafio_tecnico_amcom.stream.producer;

import com.amcom.desafio_tecnico_amcom.model.Event.ProcessedOrderEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static com.amcom.desafio_tecnico_amcom.support.constant.BrokerConstant.Topics.ORDER_PROCESSED_TOPIC;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderProducer - Testes unitários")
class OrderProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderProducer orderProducer;

    @Test
    @DisplayName("Envia ProcessedOrderEvent com sucesso para o tópico")
    void deveEnviarProcessedOrderEventComSucesso() throws Exception {
        String externalId = "ext-123";
        String json = "{\"ok\":true}";
        ProcessedOrderEvent event = mockProcessedOrderEvent(externalId);
        when(objectMapper.writeValueAsString(event)).thenReturn(json);

        orderProducer.sendProcessedOrderEvent(event);

        verify(objectMapper).writeValueAsString(event);
        verify(kafkaTemplate).send(ORDER_PROCESSED_TOPIC, externalId, json);
        verifyNoMoreInteractions(kafkaTemplate);
    }

    @Test
    @DisplayName("Lança RuntimeException quando falha a serialização do evento")
    void deveLancarRuntimeExceptionQuandoFalharSerializacao() throws Exception {
        ProcessedOrderEvent event = mock(ProcessedOrderEvent.class); // sem stubbing de getExternalId()
        when(objectMapper.writeValueAsString(event)).thenThrow(new JsonProcessingException("boom") {});

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderProducer.sendProcessedOrderEvent(event));
        assertTrue(ex.getMessage().contains("Failed to serialize ProcessedOrderEvent to JSON"));

        verify(objectMapper).writeValueAsString(event);
        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }

    private ProcessedOrderEvent mockProcessedOrderEvent(String externalId) {
        ProcessedOrderEvent event = mock(ProcessedOrderEvent.class);
        when(event.getExternalId()).thenReturn(externalId);
        return event;
    }
}