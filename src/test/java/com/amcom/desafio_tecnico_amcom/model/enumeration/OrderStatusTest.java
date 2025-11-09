package com.amcom.desafio_tecnico_amcom.model.enumeration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderStatus - Testes unitários")
class OrderStatusTest {

    @Test
    @DisplayName("Possui exatamente três status")
    void possuiExatamenteTresStatus() {
        assertEquals(3, OrderStatus.values().length);
        assertEquals(3, EnumSet.allOf(OrderStatus.class).size());
    }

    @Test
    @DisplayName("EnumSet contém todos os status esperados")
    void enumSetContemTodosOsStatusEsperados() {
        EnumSet<OrderStatus> all = EnumSet.allOf(OrderStatus.class);
        assertTrue(all.contains(OrderStatus.PENDING));
        assertTrue(all.contains(OrderStatus.COMPLETED));
        assertTrue(all.contains(OrderStatus.CANCELED));
    }

    @Test
    @DisplayName("valueOf retorna a constante correta para nomes válidos")
    void valueOfRetornaConstanteParaNomesValidos() {
        assertEquals(OrderStatus.PENDING, OrderStatus.valueOf("PENDING"));
        assertEquals(OrderStatus.COMPLETED, OrderStatus.valueOf("COMPLETED"));
        assertEquals(OrderStatus.CANCELED, OrderStatus.valueOf("CANCELED"));
    }

    @Test
    @DisplayName("valueOf lança IllegalArgumentException para nome inválido")
    void valueOfLancaIllegalArgumentExceptionParaNomeInvalido() {
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.valueOf("UNKNOWN"));
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.valueOf("pending"));
    }

    @Test
    @DisplayName("valueOf lança NullPointerException para nome null")
    void valueOfLancaNullPointerExceptionParaNomeNull() {
        assertThrows(NullPointerException.class, () -> OrderStatus.valueOf(null));
    }

    @Test
    @DisplayName("name retorna o nome exato da constante")
    void nameRetornaONomeExatoDaConstante() {
        assertEquals("PENDING", OrderStatus.PENDING.name());
        assertEquals("COMPLETED", OrderStatus.COMPLETED.name());
        assertEquals("CANCELED", OrderStatus.CANCELED.name());
    }
}

