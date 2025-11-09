package com.amcom.desafio_tecnico_amcom.command;

import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;
import com.amcom.desafio_tecnico_amcom.model.Event.ProcessedOrderEvent;
import com.amcom.desafio_tecnico_amcom.model.dto.in.CreateOrderItemRequest;
import com.amcom.desafio_tecnico_amcom.model.dto.in.CreateOrderRequest;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderEventType;
import com.amcom.desafio_tecnico_amcom.service.CreateOrderService;
import com.amcom.desafio_tecnico_amcom.stream.producer.OrderProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateOrderCommand - Testes unitários")
class CreateOrderCommandTest {

    @Mock
    private CreateOrderService createOrderService;

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private CreateOrderCommand createOrderCommand;

    @Test
    @DisplayName("Executa criação quando dados válidos e envia evento de processamento")
    void executaCriacaoQuandoDadosValidos() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-100")
                .eventType(OrderEventType.CREATE)
                .items(List.of(
                        new CreateOrderItemRequest("prod-1", 2, new BigDecimal("10.00")),
                        new CreateOrderItemRequest("prod-2", 1, new BigDecimal("5.50"))
                ))
                .build();

        createOrderCommand.execute(event);

        ArgumentCaptor<CreateOrderRequest> reqCaptor = ArgumentCaptor.forClass(CreateOrderRequest.class);
        ArgumentCaptor<ProcessedOrderEvent> processedCaptor = ArgumentCaptor.forClass(ProcessedOrderEvent.class);

        verify(createOrderService).execute(reqCaptor.capture());
        verify(orderProducer).sendProcessedOrderEvent(processedCaptor.capture());
        verifyNoMoreInteractions(createOrderService, orderProducer);

        CreateOrderRequest req = reqCaptor.getValue();
        assertEquals("ext-100", req.externalId());
        assertEquals(2, req.items().size());
        assertEquals("prod-1", req.items().getFirst().productId());
        assertEquals(2, req.items().getFirst().quantity());
        assertEquals(new BigDecimal("10.00"), req.items().getFirst().unitPrice());

        assertEquals("ext-100", processedCaptor.getValue().getExternalId());
    }

    @Test
    @DisplayName("Aceita item com quantidade igual a zero")
    void aceitaItemQuantidadeZero() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-101")
                .eventType(OrderEventType.CREATE)
                .items(List.of(new CreateOrderItemRequest("prod-3", 0, new BigDecimal("1.00"))))
                .build();

        createOrderCommand.execute(event);

        verify(createOrderService).execute(any(CreateOrderRequest.class));
        verify(orderProducer).sendProcessedOrderEvent(any(ProcessedOrderEvent.class));
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando lista de itens é nula")
    void lançaExcecaoQuandoListaItensNula() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-200")
                .eventType(OrderEventType.CREATE)
                .items(null)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> createOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("at least one item"));
        verifyNoInteractions(createOrderService, orderProducer);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando lista de itens é vazia")
    void lançaExcecaoQuandoListaItensVazia() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-201")
                .eventType(OrderEventType.CREATE)
                .items(List.of())
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> createOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("at least one item"));
        verifyNoInteractions(createOrderService, orderProducer);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando externalId é nulo")
    void lançaExcecaoQuandoExternalIdNulo() {
        OrderEvent event = OrderEvent.builder()
                .externalId(null)
                .eventType(OrderEventType.CREATE)
                .items(List.of(new CreateOrderItemRequest("prod-1", 1, new BigDecimal("2.00"))))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> createOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("ExternalId"));
        verifyNoInteractions(createOrderService, orderProducer);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando externalId é vazio")
    void lançaExcecaoQuandoExternalIdVazio() {
        OrderEvent event = OrderEvent.builder()
                .externalId("")
                .eventType(OrderEventType.CREATE)
                .items(List.of(new CreateOrderItemRequest("prod-1", 1, new BigDecimal("2.00"))))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> createOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("ExternalId"));
        verifyNoInteractions(createOrderService, orderProducer);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando eventType diferente de CREATE")
    void lançaExcecaoQuandoEventTypeDiferenteDeCreate() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-202")
                .eventType(OrderEventType.CANCEL)
                .items(List.of(new CreateOrderItemRequest("prod-1", 1, new BigDecimal("2.00"))))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> createOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("EventType"));
        verifyNoInteractions(createOrderService, orderProducer);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando productId do item é nulo")
    void lançaExcecaoQuandoItemProductIdNulo() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-203")
                .eventType(OrderEventType.CREATE)
                .items(List.of(new CreateOrderItemRequest(null, 1, new BigDecimal("2.00"))))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> createOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("ProductId"));
        verifyNoInteractions(createOrderService, orderProducer);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando productId do item é vazio")
    void lançaExcecaoQuandoItemProductIdVazio() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-204")
                .eventType(OrderEventType.CREATE)
                .items(List.of(new CreateOrderItemRequest("", 1, new BigDecimal("2.00"))))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> createOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("ProductId"));
        verifyNoInteractions(createOrderService, orderProducer);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando quantidade do item é negativa")
    void lançaExcecaoQuandoItemQuantidadeNegativa() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-205")
                .eventType(OrderEventType.CREATE)
                .items(List.of(new CreateOrderItemRequest("prod-1", -1, new BigDecimal("2.00"))))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> createOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("Quantity"));
        verifyNoInteractions(createOrderService, orderProducer);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando unitPrice do item é nulo")
    void lançaExcecaoQuandoItemUnitPriceNula() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-206")
                .eventType(OrderEventType.CREATE)
                .items(List.of(new CreateOrderItemRequest("prod-1", 1, null)))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> createOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("UnitPrice"));
        verifyNoInteractions(createOrderService, orderProducer);
    }

    @Test
    @DisplayName("Lança IllegalArgumentException quando unitPrice do item é zero")
    void lançaExcecaoQuandoItemUnitPriceZero() {
        OrderEvent event = OrderEvent.builder()
                .externalId("ext-207")
                .eventType(OrderEventType.CREATE)
                .items(List.of(new CreateOrderItemRequest("prod-1", 1, BigDecimal.ZERO)))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> createOrderCommand.execute(event));
        assertTrue(ex.getMessage().contains("UnitPrice"));
        verifyNoInteractions(createOrderService, orderProducer);
    }
}