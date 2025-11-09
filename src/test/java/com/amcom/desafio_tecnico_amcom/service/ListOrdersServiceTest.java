package com.amcom.desafio_tecnico_amcom.service;

import com.amcom.desafio_tecnico_amcom.model.dto.out.ListOrdersResponse;
import com.amcom.desafio_tecnico_amcom.model.entity.Order;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderStatus;
import com.amcom.desafio_tecnico_amcom.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListOrdersService - Testes unitários")
class ListOrdersServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ListOrdersService listOrdersService;

    @Test
    @DisplayName("Lista pedidos paginados e mapeia resposta")
    void deveListarPedidosPaginadosEMapearResposta() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("createdAt").descending());
        Order o1 = criarPedido("ext-1", OrderStatus.CANCELED, new BigDecimal("10.50"));
        Order o2 = criarPedido("ext-2", OrderStatus.COMPLETED, new BigDecimal("99.99"));
        Page<Order> paginaPedidos = criarPagina(List.of(o1, o2), pageable, 2);

        when(orderRepository.findAll(pageable)).thenReturn(paginaPedidos);

        Page<ListOrdersResponse> resposta = listOrdersService.execute(pageable);

        assertEquals(2, resposta.getTotalElements());
        assertEquals(2, resposta.getContent().size());

        ListOrdersResponse r1 = resposta.getContent().getFirst();
        assertEquals(o1.getExternalId(), r1.getExternalId());
        assertEquals(o1.getStatus(), r1.getStatus());
        assertEquals(o1.getTotalValue().toString(), r1.getTotalValue());

        ListOrdersResponse r2 = resposta.getContent().get(1);
        assertEquals(o2.getExternalId(), r2.getExternalId());
        assertEquals(o2.getStatus(), r2.getStatus());
        assertEquals(o2.getTotalValue().toString(), r2.getTotalValue());

        verify(orderRepository).findAll(pageable);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Retorna página vazia quando não há pedidos")
    void deveRetornarPaginaVaziaQuandoNaoHaPedidos() {
        Pageable pageable = PageRequest.of(1, 5);
        Page<Order> paginaVazia = criarPagina(List.of(), pageable, 0);

        when(orderRepository.findAll(pageable)).thenReturn(paginaVazia);

        Page<ListOrdersResponse> resposta = listOrdersService.execute(pageable);

        assertTrue(resposta.isEmpty());
        assertEquals(0, resposta.getTotalElements());

        verify(orderRepository).findAll(pageable);
        verifyNoMoreInteractions(orderRepository);
    }

    private Order criarPedido(String externalId, OrderStatus status, BigDecimal totalValue) {
        Order o = new Order();
        o.setExternalId(externalId);
        o.setStatus(status);
        o.setTotalValue(totalValue);
        return o;
    }

    private Page<Order> criarPagina(List<Order> conteudo, Pageable pageable, long total) {
        return new PageImpl<>(conteudo, pageable, total);
    }
}