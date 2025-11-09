package com.amcom.desafio_tecnico_amcom.controller.rest;

import com.amcom.desafio_tecnico_amcom.model.dto.out.FindOrderResponse;
import com.amcom.desafio_tecnico_amcom.model.dto.out.ListOrdersResponse;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderStatus;
import com.amcom.desafio_tecnico_amcom.service.FindOrderService;
import com.amcom.desafio_tecnico_amcom.service.ListOrdersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderController - Testes unitários")
class OrderControllerTest {

    @Mock
    private ListOrdersService listOrdersService;

    @Mock
    private FindOrderService findOrderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    @DisplayName("Lista pedidos e retorna HTTP 200 com página de respostas")
    void deveListarPedidosERetornarOk() {
        Pageable pageable = PageRequest.of(0, 10);
        ListOrdersResponse resposta1 = criarListOrdersResponse("ext-1", OrderStatus.PENDING, "100.00");
        ListOrdersResponse resposta2 = criarListOrdersResponse("ext-2", OrderStatus.COMPLETED, "250.50");
        Page<ListOrdersResponse> paginaRespostas = criarPagina(List.of(resposta1, resposta2), pageable, 2);

        when(listOrdersService.execute(pageable)).thenReturn(paginaRespostas);

        ResponseEntity<Page<ListOrdersResponse>> response = orderController.ListOrders(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("ext-1", response.getBody().getContent().get(0).getExternalId());
        assertEquals("ext-2", response.getBody().getContent().get(1).getExternalId());

        verify(listOrdersService).execute(pageable);
        verifyNoMoreInteractions(listOrdersService);
        verifyNoInteractions(findOrderService);
    }

    @Test
    @DisplayName("Lista pedidos e retorna página vazia quando não há resultados")
    void deveListarPedidosERetornarPaginaVazia() {
        Pageable pageable = PageRequest.of(1, 5);
        Page<ListOrdersResponse> paginaVazia = criarPagina(List.of(), pageable, 0);

        when(listOrdersService.execute(pageable)).thenReturn(paginaVazia);

        ResponseEntity<Page<ListOrdersResponse>> response = orderController.ListOrders(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(listOrdersService).execute(pageable);
        verifyNoMoreInteractions(listOrdersService);
        verifyNoInteractions(findOrderService);
    }

    @Test
    @DisplayName("Busca pedido por ID externo e retorna HTTP 200 com resposta")
    void deveBuscarPedidoPorIdERetornarOk() {
        String externalId = "ext-999";
        FindOrderResponse resposta = criarFindOrderResponse(externalId, OrderStatus.CANCELED, "75.25");

        when(findOrderService.execute(externalId)).thenReturn(resposta);

        ResponseEntity<FindOrderResponse> response = orderController.findOrder(externalId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(externalId, response.getBody().getExternalId());
        assertEquals(OrderStatus.CANCELED, response.getBody().getStatus());
        assertEquals("75.25", response.getBody().getTotalValue());

        verify(findOrderService).execute(externalId);
        verifyNoMoreInteractions(findOrderService);
        verifyNoInteractions(listOrdersService);
    }

    private ListOrdersResponse criarListOrdersResponse(String externalId, OrderStatus status, String totalValue) {
        ListOrdersResponse resposta = new ListOrdersResponse();
        resposta.setExternalId(externalId);
        resposta.setStatus(status);
        resposta.setTotalValue(totalValue);
        return resposta;
    }

    private FindOrderResponse criarFindOrderResponse(String externalId, OrderStatus status, String totalValue) {
        FindOrderResponse resposta = new FindOrderResponse();
        resposta.setExternalId(externalId);
        resposta.setStatus(status);
        resposta.setTotalValue(totalValue);
        resposta.setCreatedAt(LocalDateTime.now());
        resposta.setItems(List.of());
        return resposta;
    }

    private Page<ListOrdersResponse> criarPagina(List<ListOrdersResponse> conteudo, Pageable pageable, long total) {
        return new PageImpl<>(conteudo, pageable, total);
    }
}