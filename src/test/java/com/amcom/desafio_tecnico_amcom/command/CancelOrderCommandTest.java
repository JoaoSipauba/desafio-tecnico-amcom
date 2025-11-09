package com.amcom.desafio_tecnico_amcom.command;

import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderEventType;
import com.amcom.desafio_tecnico_amcom.service.CancelOrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CancelOrderCommand - Testes unitários")
class CancelOrderCommandTest {

    @Mock
    private CancelOrderService cancelOrderService;

    @InjectMocks
    private CancelOrderCommand cancelOrderCommand;

    @Test
    @DisplayName("Executa cancelamento quando externalId válido e eventType CANCEL")
    void executaCancelamentoQuandoDadosValidos() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-1")
                .eventType(OrderEventType.CANCEL)
                .build();

        cancelOrderCommand.execute(event);

        verify(cancelOrderService).execute("ext-1");
        verifyNoMoreInteractions(cancelOrderService);
    }

    @Test
    @DisplayName("Executa cancelamento quando eventType é nulo e externalId válido")
    void executaCancelamentoQuandoEventTypeNulo() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-2")
                .eventType(null)
                .build();

        cancelOrderCommand.execute(event);

        verify(cancelOrderService).execute("ext-2");
        verifyNoMoreInteractions(cancelOrderService);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando externalId é nulo")
    void lançaExcecaoQuandoExternalIdNulo() {
        OrderEvent event = OrderEvent.builder()
                .externalId(null)
                .eventType(OrderEventType.CANCEL)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> cancelOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("externalId"));
        verifyNoInteractions(cancelOrderService);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando externalId é vazio")
    void lançaExcecaoQuandoExternalIdVazio() {
        OrderEvent event = OrderEvent.builder()
                .externalId("")
                .eventType(OrderEventType.CANCEL)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> cancelOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("externalId"));
        verifyNoInteractions(cancelOrderService);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando eventType diferente de CANCEL")
    void lançaExcecaoQuandoEventTypeDiferenteDeCancel() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-3")
                .eventType(OrderEventType.CREATE)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> cancelOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("CANCEL"));
        verifyNoInteractions(cancelOrderService);
    }
}