package com.amcom.desafio_tecnico_amcom.service;

import com.amcom.desafio_tecnico_amcom.model.dto.out.ListOrdersResponse;
import com.amcom.desafio_tecnico_amcom.model.entity.Order;
import com.amcom.desafio_tecnico_amcom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListOrdersService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Page<ListOrdersResponse> execute(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return this.mapToResponse(orders);
    }

    private Page<ListOrdersResponse> mapToResponse(Page<Order> orders) {
        return orders.map(order -> ListOrdersResponse.builder()
                .externalId(order.getExternalId())
                .status(order.getStatus())
                .totalValue(order.getTotalValue().toString())
                .build());
    }
}
