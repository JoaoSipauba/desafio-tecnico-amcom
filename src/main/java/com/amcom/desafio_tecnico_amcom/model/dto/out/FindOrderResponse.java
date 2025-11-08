package com.amcom.desafio_tecnico_amcom.model.dto.out;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FindOrderResponse {

    private String id;
    private String externalId;
    private String status;
    private String totalValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
