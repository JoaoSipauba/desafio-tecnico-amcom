package com.amcom.desafio_tecnico_amcom.command;

import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;

public interface OrderCommand {
    void execute(OrderEvent event);
}
