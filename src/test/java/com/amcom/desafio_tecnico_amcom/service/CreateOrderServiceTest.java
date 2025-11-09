package com.amcom.desafio_tecnico_amcom.service;

import com.amcom.desafio_tecnico_amcom.model.dto.in.CreateOrderItemRequest;
import com.amcom.desafio_tecnico_amcom.model.dto.in.CreateOrderRequest;
import com.amcom.desafio_tecnico_amcom.model.entity.Order;
import com.amcom.desafio_tecnico_amcom.model.entity.OrderItem;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderStatus;
import com.amcom.desafio_tecnico_amcom.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateOrderService - Testes unitários")
class CreateOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CreateOrderService createOrderService;

    @Test
    @DisplayName("Cria pedido quando não existe e salva com status PENDING e total calculado")
    void deveCriarPedidoQuandoNaoExisteESalvar() {
        String externalId = "ext-100";
        LocalDateTime inicio = LocalDateTime.now();
        CreateOrderRequest dto = criarRequestComItens(
                externalId,
                List.of(
                        criarItem("prod-1", new BigDecimal("10.50"), 2),
                        criarItem("prod-2", new BigDecimal("5.00"), 1)
                )
        );

        when(orderRepository.findByExternalId(externalId)).thenReturn(Optional.empty());

        createOrderService.execute(dto);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).findByExternalId(externalId);
        verify(orderRepository).save(captor.capture());
        verifyNoMoreInteractions(orderRepository);

        Order salvo = captor.getValue();
        assertEquals(externalId, salvo.getExternalId());
        assertEquals(OrderStatus.PENDING, salvo.getStatus());
        assertNotNull(salvo.getCreatedAt());
        assertFalse(salvo.getCreatedAt().isBefore(inicio));
        assertEquals(2, salvo.getItems().size());
        assertItem(salvo.getItems().get(0), "prod-1", new BigDecimal("10.50"), 2);
        assertItem(salvo.getItems().get(1), "prod-2", new BigDecimal("5.00"), 1);
        assertEquals(new BigDecimal("26.00"), salvo.getTotalValue());
    }

    @Test
    @DisplayName("Não salva quando o pedido já existe")
    void naoDeveSalvarQuandoPedidoJaExiste() {
        String externalId = "ext-200";
        CreateOrderRequest dto = criarRequestComItens(
                externalId,
                List.of(criarItem("prod-9", new BigDecimal("1.00"), 1))
        );

        when(orderRepository.findByExternalId(externalId)).thenReturn(Optional.of(new Order()));

        createOrderService.execute(dto);

        verify(orderRepository).findByExternalId(externalId);
        verify(orderRepository, never()).save(any(Order.class));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Cria pedido com itens vazios e total igual a zero")
    void deveCriarPedidoComItensVaziosETotalZero() {
        String externalId = "ext-300";
        LocalDateTime inicio = LocalDateTime.now();
        CreateOrderRequest dto = criarRequestComItens(externalId, List.of());

        when(orderRepository.findByExternalId(externalId)).thenReturn(Optional.empty());

        createOrderService.execute(dto);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).findByExternalId(externalId);
        verify(orderRepository).save(captor.capture());
        verifyNoMoreInteractions(orderRepository);

        Order salvo = captor.getValue();
        assertEquals(OrderStatus.PENDING, salvo.getStatus());
        assertNotNull(salvo.getCreatedAt());
        assertFalse(salvo.getCreatedAt().isBefore(inicio));
        assertTrue(salvo.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, salvo.getTotalValue());
    }

    private CreateOrderRequest criarRequestComItens(String externalId, List<CreateOrderItemRequest> itens) {
        return new CreateOrderRequest(externalId, itens);
    }

    private CreateOrderItemRequest criarItem(String productId, BigDecimal unitPrice, int quantity) {
        return new CreateOrderItemRequest(productId, quantity, unitPrice);
    }

    private void assertItem(OrderItem item, String productId, BigDecimal unitPrice, int quantity) {
        assertEquals(productId, item.getProductId());
        assertEquals(unitPrice, item.getUnitPrice());
        assertEquals(quantity, item.getQuantity());
    }
}