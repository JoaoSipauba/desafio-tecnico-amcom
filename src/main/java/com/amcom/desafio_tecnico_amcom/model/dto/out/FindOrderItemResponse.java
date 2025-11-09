package com.amcom.desafio_tecnico_amcom.model.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FindOrderItemResponse {

    @Schema(description = "Product ID associated with the order item", example = "prod-12345")
    private String productId;

    @Schema(description = "Quantity of the product ordered", example = "2")
    private Integer quantity;

    @Schema(description = "Unit price of the product", example = "75.00")
    private BigDecimal unitPrice;
}
