package com.amcom.desafio_tecnico_amcom.stream.consumer;

import com.amcom.desafio_tecnico_amcom.command.OrderCommand;
import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderConsumer - Testes unitários")
class OrderConsumerTest {

    @Mock
    private Map<String, OrderCommand> commandMap;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Processa mensagem válida e executa o comando correspondente")
    void processaMensagemValidaEExecutaComandoCorrespondente() {
        String message = "{\"eventType\":\"CREATE\"}";
        OrderCommand command = mock(OrderCommand.class);
        when(commandMap.get("CREATE")).thenReturn(command);

        OrderConsumer consumer = new OrderConsumer(commandMap, new ObjectMapper());
        consumer.listen(message);

        verify(commandMap).get("CREATE");
        verify(command).execute(any(OrderEvent.class));
        verifyNoMoreInteractions(commandMap, command);
    }

    @Test
    @DisplayName("Lança RuntimeException quando JSON é inválido")
    void lancaRuntimeExceptionQuandoJsonInvalido() throws Exception {
        OrderConsumer consumer = new OrderConsumer(commandMap, objectMapper);
        when(objectMapper.readValue(anyString(), eq(OrderEvent.class))).thenThrow(new RuntimeException("invalid"));

        assertThrows(RuntimeException.class, () -> consumer.listen("{invalid-json}"));

        verify(objectMapper).readValue(anyString(), eq(OrderEvent.class));
        verifyNoInteractions(commandMap);
    }

    @Test
    @DisplayName("Lança RuntimeException quando nenhum comando é encontrado para o tipo do evento")
    void lancaRuntimeExceptionQuandoNenhumComandoEncontrado() {
        String message = "{\"eventType\":\"CREATE\"}";
        OrderConsumer consumer = new OrderConsumer(commandMap, new ObjectMapper());

        assertThrows(RuntimeException.class, () -> consumer.listen(message));

        verify(commandMap).get("CREATE");
        verifyNoMoreInteractions(commandMap);
    }

    @Test
    @DisplayName("Lança RuntimeException quando a execução do comando falha")
    void lancaRuntimeExceptionQuandoExecucaoDoComandoFalha() {
        String message = "{\"eventType\":\"CREATE\"}";
        OrderCommand command = mock(OrderCommand.class);
        when(commandMap.get("CREATE")).thenReturn(command);
        doThrow(new RuntimeException("boom")).when(command).execute(any(OrderEvent.class));

        OrderConsumer consumer = new OrderConsumer(commandMap, new ObjectMapper());

        assertThrows(RuntimeException.class, () -> consumer.listen(message));

        verify(commandMap).get("CREATE");
        verify(command).execute(any(OrderEvent.class));
        verifyNoMoreInteractions(commandMap, command);
    }
}