package com.amcom.desafio_tecnico_amcom.model.dto.out;

import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FindOrderResponse {

    @Schema(description = "External ID of the order", example = "e3e6f88f-2f95-4bd0-8320-8ba18ab95ef8")
    private String externalId;

    @Schema(description = "Current status of the order", example = "PENDING")
    private OrderStatus status;

    @Schema(description = "Total value of the order", example = "150.00")
    private String totalValue;

    @Schema(description = "List of items in the order")
    private List<FindOrderItemResponse> items;

    @Schema(description = "Timestamp when the order was created", example = "2024-01-01T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the order was last updated", example = "2024-01-02T15:30:00")
    private LocalDateTime updatedAt;
}
