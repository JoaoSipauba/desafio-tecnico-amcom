package com.amcom.desafio_tecnico_amcom.model.dto.out;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FindOrderItemResponse {

    private String productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
