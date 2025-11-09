package com.amcom.desafio_tecnico_amcom.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NotFoundException - Testes unitários")
class NotFoundExceptionTest {

    @Test
    @DisplayName("Cria NotFoundException com mensagem e retorna a mesma mensagem")
    void criaComMensagemRetornaMensagem() {
        String msg = "Recurso não encontrado";
        NotFoundException ex = new NotFoundException(msg);

        assertEquals(msg, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    @DisplayName("Cria NotFoundException com mensagem null e mantém mensagem null")
    void criaComMensagemNullMantemNull() {
        NotFoundException ex = new NotFoundException(null);

        assertNull(ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    @DisplayName("Lança NotFoundException e é capturada por assertThrows")
    void lancaExcecaoAoSerLancada() {
        String msg = "Item inexistente";
        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException(msg);
        });
        assertEquals(msg, ex.getMessage());
    }
}