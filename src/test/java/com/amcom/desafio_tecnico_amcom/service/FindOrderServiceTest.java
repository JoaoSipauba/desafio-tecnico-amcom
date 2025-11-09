package com.amcom.desafio_tecnico_amcom.service;

import com.amcom.desafio_tecnico_amcom.exception.NotFoundException;
import com.amcom.desafio_tecnico_amcom.model.dto.out.FindOrderResponse;
import com.amcom.desafio_tecnico_amcom.model.entity.Order;
import com.amcom.desafio_tecnico_amcom.model.entity.OrderItem;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderStatus;
import com.amcom.desafio_tecnico_amcom.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@DisplayName("FindOrderService - Testes unitários")
class FindOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private FindOrderService findOrderService;

    @Test
    @DisplayName("Retorna dados do pedido existente")
    void deveRetornarPedidoQuandoExistente() {
        String externalId = "ord-999";
        Order pedido = criarPedidoComItens(externalId);

        when(orderRepository.findByExternalId(externalId)).thenReturn(Optional.of(pedido));

        FindOrderResponse resposta = findOrderService.execute(externalId);

        assertEquals(pedido.getExternalId(), resposta.getExternalId());
        assertEquals(pedido.getStatus(), resposta.getStatus());
        assertEquals(pedido.getTotalValue().toString(), resposta.getTotalValue());
        assertEquals(pedido.getCreatedAt(), resposta.getCreatedAt());
        assertEquals(pedido.getUpdatedAt(), resposta.getUpdatedAt());
        assertEquals(pedido.getItems().size(), resposta.getItems().size());
        assertEquals(pedido.getItems().getFirst().getProductId(), resposta.getItems().getFirst().getProductId());
        assertEquals(pedido.getItems().getFirst().getQuantity(), resposta.getItems().getFirst().getQuantity());
        assertEquals(pedido.getItems().getFirst().getUnitPrice(), resposta.getItems().getFirst().getUnitPrice());

        verify(orderRepository).findByExternalId(externalId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Lança NotFoundException quando o pedido não existe")
    void deveLancarNotFoundExceptionQuandoPedidoNaoExiste() {
        String externalId = "inexistente";

        when(orderRepository.findByExternalId(externalId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> findOrderService.execute(externalId));
        assertTrue(ex.getMessage().toLowerCase().contains("order"));

        verify(orderRepository).findByExternalId(externalId);
        verifyNoMoreInteractions(orderRepository);
    }

    private Order criarPedidoComItens(String externalId) {
        Order pedido = new Order();
        pedido.setExternalId(externalId);
        pedido.setStatus(OrderStatus.CANCELED);
        pedido.setTotalValue(new BigDecimal("150.75"));
        pedido.setCreatedAt(LocalDateTime.now().minusHours(5));
        pedido.setUpdatedAt(LocalDateTime.now().minusHours(1));

        OrderItem item1 = criarItem("prod-1", 2, new BigDecimal("50.00"));
        OrderItem item2 = criarItem("prod-2", 1, new BigDecimal("50.75"));
        pedido.setItems(List.of(item1, item2));
        return pedido;
    }

    private OrderItem criarItem(String productId, int quantidade, BigDecimal unitPrice) {
        OrderItem item = new OrderItem();
        item.setProductId(productId);
        item.setQuantity(quantidade);
        item.setUnitPrice(unitPrice);
        return item;
    }
}