package otavio.machado;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CartaTest {

    @Test
    void deveConservarValorENaipeRecebidos() {
        Carta carta = new Carta(ValorCarta.DAMA, Naipe.COPAS);

        assertAll(
            () -> assertSame(ValorCarta.DAMA, carta.getValor()),
            () -> assertEquals("Q", carta.getValorNominal()),
            () -> assertSame(Naipe.COPAS, carta.getNaipe())
        );
    }

    @Test
    void deveNormalizarValorNominalRecebidoComoTexto() {
        Carta carta = new Carta(" q ", Naipe.OUROS);

        assertSame(ValorCarta.DAMA, carta.getValor());
    }

    @Test
    void deveRejeitarValorOuNaipeInvalidos() {
        assertAll(
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new Carta((ValorCarta) null, Naipe.COPAS)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new Carta(ValorCarta.AS, null)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new Carta("   ", Naipe.COPAS)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new Carta("14", Naipe.COPAS)
            )
        );
    }

    @Test
    void deveCompararCartasPorValorENaipe() {
        Carta primeira = new Carta(ValorCarta.REI, Naipe.ESPADAS);
        Carta equivalente = new Carta(ValorCarta.REI, Naipe.ESPADAS);
        Carta outroValor = new Carta(ValorCarta.DAMA, Naipe.ESPADAS);
        Carta outroNaipe = new Carta(ValorCarta.REI, Naipe.PAUS);

        assertAll(
            () -> assertEquals(primeira, equivalente),
            () -> assertEquals(primeira.hashCode(), equivalente.hashCode()),
            () -> assertNotEquals(primeira, outroValor),
            () -> assertNotEquals(primeira, outroNaipe),
            () -> assertNotEquals(primeira, null),
            () -> assertNotEquals(primeira, "K♠")
        );
    }

    @Test
    void deveProduzirRepresentacaoLegivel() {
        Carta carta = new Carta(ValorCarta.DAMA, Naipe.COPAS);

        assertEquals("Q♥", carta.toString());
    }
}
