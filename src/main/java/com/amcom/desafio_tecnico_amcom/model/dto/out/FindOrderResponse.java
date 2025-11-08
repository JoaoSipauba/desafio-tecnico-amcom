package com.amcom.desafio_tecnico_amcom.model.dto.out;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FindOrderResponse {

    private String id;
    private String externalId;
    private String status;
    private String totalValue;
    private List<FindOrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
