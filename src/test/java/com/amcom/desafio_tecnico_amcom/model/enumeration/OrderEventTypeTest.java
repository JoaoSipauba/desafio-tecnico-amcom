package com.amcom.desafio_tecnico_amcom.model.enumeration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderEventType - Testes unitários")
class OrderEventTypeTest {

    @Test
    @DisplayName("Possui exatamente três tipos de evento")
    void possuiExatamenteTresTiposDeEvento() {
        assertEquals(3, OrderEventType.values().length);
        assertEquals(3, EnumSet.allOf(OrderEventType.class).size());
    }

    @Test
    @DisplayName("EnumSet contém todos os tipos esperados")
    void enumSetContemTodosOsTiposEsperados() {
        EnumSet<OrderEventType> all = EnumSet.allOf(OrderEventType.class);
        assertTrue(all.contains(OrderEventType.CREATE));
        assertTrue(all.contains(OrderEventType.CANCEL));
        assertTrue(all.contains(OrderEventType.COMPLETE));
    }

    @Test
    @DisplayName("valueOf retorna a constante correta para nomes válidos")
    void valueOfRetornaConstanteParaNomesValidos() {
        assertEquals(OrderEventType.CREATE, OrderEventType.valueOf("CREATE"));
        assertEquals(OrderEventType.CANCEL, OrderEventType.valueOf("CANCEL"));
        assertEquals(OrderEventType.COMPLETE, OrderEventType.valueOf("COMPLETE"));
    }

    @Test
    @DisplayName("valueOf lança IllegalArgumentException para nome inválido")
    void valueOfLancaIllegalArgumentExceptionParaNomeInvalido() {
        assertThrows(IllegalArgumentException.class, () -> OrderEventType.valueOf("UNKNOWN"));
        assertThrows(IllegalArgumentException.class, () -> OrderEventType.valueOf("create"));
    }

    @Test
    @DisplayName("valueOf lança NullPointerException para nome null")
    void valueOfLancaNullPointerExceptionParaNomeNull() {
        assertThrows(NullPointerException.class, () -> OrderEventType.valueOf(null));
    }

    @Test
    @DisplayName("name retorna o nome exato da constante")
    void nameRetornaONomeExatoDaConstante() {
        assertEquals("CREATE", OrderEventType.CREATE.name());
        assertEquals("CANCEL", OrderEventType.CANCEL.name());
        assertEquals("COMPLETE", OrderEventType.COMPLETE.name());
    }
}