package com.amcom.desafio_tecnico_amcom.model.Event;

import com.amcom.desafio_tecnico_amcom.model.dto.in.CreateOrderItemRequest;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String externalId;
    private List<CreateOrderItemRequest> items;
    private OrderEventType eventType;
}