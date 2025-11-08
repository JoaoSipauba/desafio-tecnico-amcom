package com.amcom.desafio_tecnico_amcom.service;

import com.amcom.desafio_tecnico_amcom.exception.NotFoundException;
import com.amcom.desafio_tecnico_amcom.model.dto.out.FindDetailedOrderItemResponse;
import com.amcom.desafio_tecnico_amcom.model.dto.out.FindDetailedOrderResponse;
import com.amcom.desafio_tecnico_amcom.model.entity.Order;
import com.amcom.desafio_tecnico_amcom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindDetailedOrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public FindDetailedOrderResponse execute(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        return this.mapToResponse(order);
    }

    private FindDetailedOrderResponse mapToResponse(Order order) {
        List<FindDetailedOrderItemResponse> items = order.getItems().stream()
                .map(item -> FindDetailedOrderItemResponse.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build())
                .toList();

        return FindDetailedOrderResponse.builder()
                .id(order.getId())
                .externalId(order.getExternalId())
                .status(order.getStatus().name())
                .totalValue(order.getTotalValue().toString())
                .items(items)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

}
