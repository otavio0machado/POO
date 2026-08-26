package otavio.machado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class JogadorTest {

    @Test
    void deveConservarNomeCompletoRemovendoEspacosExternos() {
        Jogador jogador = new Jogador("  Otavio Machado  ");

        assertEquals("Otavio Machado", jogador.getNome());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   \t\n"})
    void deveRejeitarNomeNuloOuEmBranco(String nome) {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Jogador(nome)
        );
    }

    @Test
    void deveConservarUmaMesmaMaoDuranteSuaVida() {
        Jogador jogador = new Jogador("Otavio");

        Mao primeiraConsulta = jogador.getMao();
        Mao segundaConsulta = jogador.getMao();

        assertSame(primeiraConsulta, segundaConsulta);
    }

    @Test
    void jogadoresDiferentesDevemTerMaosIndependentes() {
        Jogador primeiro = new Jogador("Otavio");
        Jogador segundo = new Jogador("Ana");

        primeiro.getMao().adicionarCarta(
            new Carta(ValorCarta.AS, Naipe.COPAS)
        );

        assertNotSame(primeiro.getMao(), segundo.getMao());
        assertEquals(1, primeiro.getMao().getQuantidadeCartas());
        assertEquals(0, segundo.getMao().getQuantidadeCartas());
    }
}
