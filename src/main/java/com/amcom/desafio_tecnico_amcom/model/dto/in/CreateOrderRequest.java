package com.amcom.desafio_tecnico_amcom.model.dto.in;

import java.util.List;

public record CreateOrderRequest(
        String externalId,
        List<CreateOrderItemRequest> items
) {
}
