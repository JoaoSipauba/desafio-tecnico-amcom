package com.amcom.desafio_tecnico_amcom.service;

import com.amcom.desafio_tecnico_amcom.model.dto.in.CreateOrderRequest;
import com.amcom.desafio_tecnico_amcom.model.entity.Order;
import com.amcom.desafio_tecnico_amcom.model.entity.OrderItem;
import com.amcom.desafio_tecnico_amcom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void execute(CreateOrderRequest dto) {
        var existingOrder = orderRepository.findByExternalId(dto.externalId()).stream().findFirst();

        if (existingOrder.isPresent()) {
            return;
        }

        Order order = this.mapToEntity(dto);
        order.setTotalValue(this.calculateTotal(order));
    }

    private Order mapToEntity(CreateOrderRequest dto) {
        var items = dto.items().stream()
                .map(i -> OrderItem.builder()
                        .productId(i.productId())
                        .unitPrice(i.unitPrice())
                        .quantity(i.quantity())
                        .build())
                .toList();


        return Order.builder()
                .externalId(dto.externalId())
                .items(items)
                .build();
    }

    private BigDecimal calculateTotal(Order order) {
        return order.getItems().stream().map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
