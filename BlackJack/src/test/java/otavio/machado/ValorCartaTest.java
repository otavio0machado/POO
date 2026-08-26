package otavio.machado;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class ValorCartaTest {

    @Test
    void deveConterTrezeValoresComSimbolosUnicos() {
        ValorCarta[] valores = ValorCarta.values();
        Set<String> simbolos = Arrays.stream(valores)
            .map(ValorCarta::getSimbolo)
            .collect(Collectors.toSet());

        assertAll(
            () -> assertEquals(13, valores.length),
            () -> assertEquals(13, simbolos.size())
        );
    }

    @Test
    void deveInformarOsValoresBaseDoBlackjack() {
        assertAll(
            () -> assertEquals(11, ValorCarta.AS.getValorBase()),
            () -> assertEquals(2, ValorCarta.DOIS.getValorBase()),
            () -> assertEquals(10, ValorCarta.DEZ.getValorBase()),
            () -> assertEquals(10, ValorCarta.VALETE.getValorBase()),
            () -> assertEquals(10, ValorCarta.DAMA.getValorBase()),
            () -> assertEquals(10, ValorCarta.REI.getValorBase())
        );
    }

    @Test
    void deveIdentificarSomenteOAs() {
        assertTrue(ValorCarta.AS.ehAs());

        for (ValorCarta valor : ValorCarta.values()) {
            if (valor != ValorCarta.AS) {
                assertFalse(valor.ehAs());
            }
        }
    }

    @Test
    void deveConverterSimboloIgnorandoEspacosECaixa() {
        assertAll(
            () -> assertSame(ValorCarta.AS, ValorCarta.deSimbolo(" a ")),
            () -> assertSame(ValorCarta.DEZ, ValorCarta.deSimbolo(" 10 ")),
            () -> assertSame(ValorCarta.DAMA, ValorCarta.deSimbolo(" q "))
        );
    }

    @Test
    void deveRejeitarSimboloNuloVazioOuInexistente() {
        assertAll(
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> ValorCarta.deSimbolo(null)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> ValorCarta.deSimbolo("   ")
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> ValorCarta.deSimbolo("XPTO")
            )
        );
    }
}
