package com.amcom.desafio_tecnico_amcom.command;

import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderEventType;
import com.amcom.desafio_tecnico_amcom.service.CancelOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("CANCEL")
@RequiredArgsConstructor
public class CancelOrderCommand implements OrderCommand {

    private final CancelOrderService cancelOrderService;

    @Override
    public void execute(OrderEvent event) {
        this.validate(event);
        cancelOrderService.execute(event.getExternalId());
    }

    private void validate(OrderEvent event) {
        if (event.getExternalId() == null || event.getExternalId().isEmpty()) {
            throw new IllegalArgumentException("The externalId field is required to cancel the order.");
        }

        if (event.getEventType() != null && event.getEventType() != OrderEventType.CANCEL) {
            throw new IllegalArgumentException(
                    "The event type must be CANCEL to cancel an order."
            );
        }
    }

}
