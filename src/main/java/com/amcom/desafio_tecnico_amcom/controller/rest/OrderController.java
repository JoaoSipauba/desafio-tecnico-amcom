package com.amcom.desafio_tecnico_amcom.controller.rest;

import com.amcom.desafio_tecnico_amcom.controller.api.OrderResource;
import com.amcom.desafio_tecnico_amcom.model.dto.out.FindOrderResponse;
import com.amcom.desafio_tecnico_amcom.model.dto.out.ListOrdersResponse;
import com.amcom.desafio_tecnico_amcom.service.FindOrderService;
import com.amcom.desafio_tecnico_amcom.service.ListOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController implements OrderResource {

    private final ListOrdersService listOrdersService;
    private final FindOrderService findOrderService;

    @Override
    public ResponseEntity<Page<ListOrdersResponse>> ListOrders(Pageable pageable) {
        Page<ListOrdersResponse> response = listOrdersService.execute(pageable);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<FindOrderResponse> findOrder(String externalId) {
        FindOrderResponse response = findOrderService.execute(externalId);
        return ResponseEntity.ok(response);
    }
}
