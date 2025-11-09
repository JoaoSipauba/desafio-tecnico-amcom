package com.amcom.desafio_tecnico_amcom.command;

import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;
import com.amcom.desafio_tecnico_amcom.model.Event.ProcessedOrderEvent;
import com.amcom.desafio_tecnico_amcom.model.dto.in.CreateOrderItemRequest;
import com.amcom.desafio_tecnico_amcom.model.dto.in.CreateOrderRequest;
import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderEventType;
import com.amcom.desafio_tecnico_amcom.service.CreateOrderService;
import com.amcom.desafio_tecnico_amcom.stream.producer.ProcessedOrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("CREATE")
@RequiredArgsConstructor
public class CreateOrderCommand implements OrderCommand {

    private final CreateOrderService createOrderService;
    private final ProcessedOrderProducer processedOrderProducer;

    @Override
    public void execute(OrderEvent event) {
        this.validate(event);
        CreateOrderRequest request = mapToCreateOrderRequest(event);
        createOrderService.execute(request);

        processedOrderProducer.sendProcessedOrderEvent(new ProcessedOrderEvent(event.getExternalId()));
    }

    private void validate(OrderEvent event) {
        if (event.getItems() == null || event.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }

        if (event.getExternalId() == null || event.getExternalId().isEmpty()) {
            throw new IllegalArgumentException("ExternalId must not be null or empty.");
        }

        if (event.getEventType() != null && event.getEventType() != OrderEventType.CREATE) {
            throw new IllegalArgumentException("EventType must be CREATE to create an order.");
        }

        for (var item : event.getItems()) {
            if (item.productId() == null || item.productId().isEmpty()) {
                throw new IllegalArgumentException("ProductId must not be null or empty.");
            }
            if (item.quantity() < 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0.");
            }
            if (item.unitPrice() == null || item.unitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("UnitPrice must be greater or equal to 0.");
            }
        }

    }

    private CreateOrderRequest mapToCreateOrderRequest(OrderEvent event) {
        List<CreateOrderItemRequest> itemRequestList = event.getItems().stream()
                .map(item -> new CreateOrderItemRequest(item.productId(), item.quantity(), item.unitPrice())).toList();
        return new CreateOrderRequest(event.getExternalId(), itemRequestList);
    }
}
