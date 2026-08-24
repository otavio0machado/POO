package otavio.machado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void deveCriarBaralhoComCinquentaEDuasCartas() {
        Baralho baralho = new Baralho();

        assertEquals(52, baralho.getQuantidadeCartas());
    }

    @Test
    void deveManterOsDadosDaCarta() {
        Carta carta = new Carta(" q ", Naipe.COPAS);

        assertEquals("Q", carta.getValorNominal());
        assertEquals(Naipe.COPAS, carta.getNaipe());
    }

    @Test
    void deveImpedirCartaSemNaipe() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Carta("A", null)
        );
    }
}
