package com.amcom.desafio_tecnico_amcom.model.dto.out;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListOrdersResponse {

    private String id;
    private String externalId;
    private String status;
    private String totalValue;
}
