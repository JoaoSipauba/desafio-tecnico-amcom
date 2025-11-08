package com.amcom.desafio_tecnico_amcom.service;

import com.amcom.desafio_tecnico_amcom.exception.NotFoundException;
import com.amcom.desafio_tecnico_amcom.model.dto.out.FindOrderResponse;
import com.amcom.desafio_tecnico_amcom.model.entity.Order;
import com.amcom.desafio_tecnico_amcom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindOrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public FindOrderResponse execute(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        return this.mapToResponse(order);
    }

    private FindOrderResponse mapToResponse(Order order) {
        return FindOrderResponse.builder()
                .id(order.getId())
                .externalId(order.getExternalId())
                .status(order.getStatus().name())
                .totalValue(order.getTotalValue().toString())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
