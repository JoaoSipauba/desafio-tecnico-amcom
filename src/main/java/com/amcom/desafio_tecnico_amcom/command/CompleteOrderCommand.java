package com.amcom.desafio_tecnico_amcom.command;

import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderEventType;
import com.amcom.desafio_tecnico_amcom.service.CompleteOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("COMPLETE")
@RequiredArgsConstructor
public class CompleteOrderCommand implements OrderCommand {

    private final CompleteOrderService completeOrderService;

    @Override
    public void execute(OrderEvent event) {
        this.validate(event);
        completeOrderService.execute(event.getExternalId());
    }

    private void validate(OrderEvent event) {
        if (event.getExternalId() == null || event.getExternalId().isEmpty()) {
            throw new IllegalArgumentException("The externalId field is required to complete the order.");
        }

        if (event.getEventType() != null && event.getEventType() != OrderEventType.COMPLETE) {
            throw new IllegalArgumentException(
                    "The event type must be COMPLETE to complete an order."
            );
        }
    }
}
