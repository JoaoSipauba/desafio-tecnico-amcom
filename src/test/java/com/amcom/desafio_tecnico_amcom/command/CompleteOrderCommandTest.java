package com.amcom.desafio_tecnico_amcom.command;

import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderEventType;
import com.amcom.desafio_tecnico_amcom.service.CompleteOrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompleteOrderCommand - Testes unitários")
class CompleteOrderCommandTest {

    @Mock
    private CompleteOrderService completeOrderService;

    @InjectMocks
    private CompleteOrderCommand completeOrderCommand;

    @Test
    @DisplayName("Executa conclusão quando externalId válido e eventType COMPLETE")
    void executaConclusaoQuandoDadosValidos() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-10")
                .eventType(OrderEventType.COMPLETE)
                .build();

        completeOrderCommand.execute(event);

        verify(completeOrderService).execute("ext-10");
        verifyNoMoreInteractions(completeOrderService);
    }

    @Test
    @DisplayName("Executa conclusão quando eventType é nulo e externalId válido")
    void executaConclusaoQuandoEventTypeNulo() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-11")
                .eventType(null)
                .build();

        completeOrderCommand.execute(event);

        verify(completeOrderService).execute("ext-11");
        verifyNoMoreInteractions(completeOrderService);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando externalId é nulo")
    void lancaExcecaoQuandoExternalIdNulo() {
        OrderEvent event = OrderEvent.builder()
                .externalId(null)
                .eventType(OrderEventType.COMPLETE)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> completeOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("externalId"));
        verifyNoInteractions(completeOrderService);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando externalId é vazio")
    void lancaExcecaoQuandoExternalIdVazio() {
        OrderEvent event = OrderEvent.builder()
                .externalId("")
                .eventType(OrderEventType.COMPLETE)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> completeOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("externalId"));
        verifyNoInteractions(completeOrderService);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando eventType diferente de COMPLETE")
    void lancaExcecaoQuandoEventTypeDiferenteDeComplete() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-12")
                .eventType(OrderEventType.CANCEL)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> completeOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("COMPLETE"));
        verifyNoInteractions(completeOrderService);
    }
}

