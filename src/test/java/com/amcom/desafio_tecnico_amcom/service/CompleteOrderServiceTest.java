package com.amcom.desafio_tecnico_amcom.service;

import com.amcom.desafio_tecnico_amcom.exception.NotFoundException;
import com.amcom.desafio_tecnico_amcom.model.entity.Order;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderStatus;
import com.amcom.desafio_tecnico_amcom.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompleteOrderService - Testes unitários")
class CompleteOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CompleteOrderService completeOrderService;

    @Test
    @DisplayName("Completa pedido existente não completo e salva as alterações")
    void deveCompletarPedidoQuandoExistenteENaoCompleto() {
        String orderId = "ord-789";
        Order order = criarPedidoNaoCompleto();
        LocalDateTime inicio = LocalDateTime.now();

        when(orderRepository.findByExternalId(orderId)).thenReturn(Optional.of(order));

        completeOrderService.execute(orderId);

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        assertNotNull(order.getUpdatedAt());
        assertFalse(order.getUpdatedAt().isBefore(inicio));

        verify(orderRepository).findByExternalId(orderId);
        verify(orderRepository).save(order);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Não salva quando o pedido já está completo")
    void naoDeveSalvarQuandoPedidoJaEstaCompleto() {
        String orderId = "ord-456";
        Order order = criarPedidoCompleto();
        LocalDateTime atualizadoAntes = order.getUpdatedAt();

        when(orderRepository.findByExternalId(orderId)).thenReturn(Optional.of(order));

        completeOrderService.execute(orderId);

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        assertEquals(atualizadoAntes, order.getUpdatedAt());

        verify(orderRepository).findByExternalId(orderId);
        verify(orderRepository, never()).save(any(Order.class));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Lança NotFoundException quando o pedido não existe")
    void deveLancarNotFoundExceptionQuandoPedidoNaoExiste() {
        String orderId = "inexistente";

        when(orderRepository.findByExternalId(orderId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> completeOrderService.execute(orderId));
        assertTrue(ex.getMessage().contains(orderId));

        verify(orderRepository).findByExternalId(orderId);
        verify(orderRepository, never()).save(any(Order.class));
        verifyNoMoreInteractions(orderRepository);
    }

    private Order criarPedidoNaoCompleto() {
        Order o = new Order();
        o.setStatus(null);
        o.setUpdatedAt(null);
        return o;
    }

    private Order criarPedidoCompleto() {
        Order o = new Order();
        o.setStatus(OrderStatus.COMPLETED);
        o.setUpdatedAt(LocalDateTime.now().minusHours(2));
        return o;
    }
}