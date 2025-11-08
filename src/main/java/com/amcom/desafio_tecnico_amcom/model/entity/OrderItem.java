package com.amcom.desafio_tecnico_amcom.model.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItem {

    private String productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
