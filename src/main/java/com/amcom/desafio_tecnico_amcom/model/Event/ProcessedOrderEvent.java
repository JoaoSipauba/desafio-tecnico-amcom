package com.amcom.desafio_tecnico_amcom.model.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedOrderEvent {
    private String externalId;
}