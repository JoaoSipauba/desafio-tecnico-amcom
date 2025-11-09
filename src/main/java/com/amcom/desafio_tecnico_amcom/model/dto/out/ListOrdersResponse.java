package com.amcom.desafio_tecnico_amcom.model.dto.out;

import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListOrdersResponse {

    @Schema(description = "External identifier of the order", example = "e3e6f88f-2f95-4bd0-8320-8ba18ab95ef8")
    private String externalId;

    @Schema(description = "Current status of the order", example = "PENDING")
    private OrderStatus status;

    @Schema(description = "Total value of the order", example = "150.00")
    private String totalValue;
}
