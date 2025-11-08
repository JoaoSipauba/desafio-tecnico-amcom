package com.amcom.desafio_tecnico_amcom.model.dto.in;

import java.math.BigDecimal;

public record CreateOrderItemRequest(
        String productId,
        Integer quantity,
        BigDecimal unitPrice
) {
}
