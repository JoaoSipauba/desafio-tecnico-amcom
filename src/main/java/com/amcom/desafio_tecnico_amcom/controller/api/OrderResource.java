package com.amcom.desafio_tecnico_amcom.controller.api;

import com.amcom.desafio_tecnico_amcom.model.dto.out.FindOrderResponse;
import com.amcom.desafio_tecnico_amcom.model.dto.out.ListOrdersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/orders")
public interface OrderResource {

    @GetMapping
    ResponseEntity<Page<ListOrdersResponse>> ListOrders(Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<FindOrderResponse> findOrder(@PathVariable String id);
}
