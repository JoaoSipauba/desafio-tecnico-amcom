package com.amcom.desafio_tecnico_amcom.service;

import com.amcom.desafio_tecnico_amcom.exception.NotFoundException;
import com.amcom.desafio_tecnico_amcom.model.dto.out.FindOrderItemResponse;
import com.amcom.desafio_tecnico_amcom.model.dto.out.FindOrderResponse;
import com.amcom.desafio_tecnico_amcom.model.entity.Order;
import com.amcom.desafio_tecnico_amcom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindOrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public FindOrderResponse execute(String id) {
        Order order = orderRepository.findByExternalId(id).orElseThrow(() -> new NotFoundException("Order not found"));
        return this.mapToResponse(order);
    }

    private FindOrderResponse mapToResponse(Order order) {
        List<FindOrderItemResponse> items = order.getItems().stream()
                .map(item -> FindOrderItemResponse.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build())
                .toList();

        return FindOrderResponse.builder()
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
