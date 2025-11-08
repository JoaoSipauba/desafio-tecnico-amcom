package com.amcom.desafio_tecnico_amcom.model.entity;

import com.amcom.desafio_tecnico_amcom.model.enumeration.OrderStatus;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @Indexed(unique = true)
    private String externalId;

    private OrderStatus status = OrderStatus.PENDING;

    private BigDecimal totalValue = BigDecimal.ZERO;

    private List<OrderItem> items = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}