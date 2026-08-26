package otavio.machado;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DealerTest {

    @Test
    void deveConservarUmaMesmaMaoDuranteSuaVida() {
        Dealer dealer = new Dealer();

        assertSame(dealer.getMao(), dealer.getMao());
    }

    @Test
    void dealerEJogadorDevemTerMaosIndependentes() {
        Dealer dealer = new Dealer();
        Jogador jogador = new Jogador("Otavio");

        dealer.getMao().adicionarCarta(
            new Carta(ValorCarta.REI, Naipe.ESPADAS)
        );

        assertNotSame(dealer.getMao(), jogador.getMao());
        assertEquals(1, dealer.getMao().getQuantidadeCartas());
        assertEquals(0, jogador.getMao().getQuantidadeCartas());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cenariosDaPoliticaDeCompra")
    void deveAplicarPoliticaDePararEmDezesseteInclusiveSoft17(
        String descricao,
        List<ValorCarta> valores,
        int pontuacaoEsperada,
        boolean maciaEsperada,
        boolean deveComprarEsperado
    ) {
        Dealer dealer = dealerCom(valores);

        assertAll(
            () -> assertEquals(
                pontuacaoEsperada,
                dealer.getMao().calcularPontuacao(),
                descricao
            ),
            () -> assertEquals(
                maciaEsperada,
                dealer.getMao().ehMacia(),
                descricao
            ),
            () -> assertEquals(
                deveComprarEsperado,
                dealer.deveComprar(),
                descricao
            )
        );
    }

    @Test
    void deveReavaliarAMaoDepoisDeCadaCarta() {
        Dealer dealer = dealerCom(List.of(ValorCarta.AS, ValorCarta.CINCO));

        assertAll(
            () -> assertEquals(16, dealer.getMao().calcularPontuacao()),
            () -> assertTrue(dealer.getMao().ehMacia()),
            () -> assertTrue(dealer.deveComprar())
        );

        dealer.getMao().adicionarCarta(
            new Carta(ValorCarta.AS, Naipe.PAUS)
        );

        assertAll(
            () -> assertEquals(17, dealer.getMao().calcularPontuacao()),
            () -> assertTrue(dealer.getMao().ehMacia()),
            () -> assertFalse(dealer.deveComprar())
        );
    }

    private static Stream<Arguments> cenariosDaPoliticaDeCompra() {
        return Stream.of(
            cenario("16 duro compra", 16, false, true,
                ValorCarta.DEZ, ValorCarta.SEIS),
            cenario("soft 16 compra", 16, true, true,
                ValorCarta.AS, ValorCarta.CINCO),
            cenario("17 duro para", 17, false, false,
                ValorCarta.DEZ, ValorCarta.SETE),
            cenario("soft 17 para", 17, true, false,
                ValorCarta.AS, ValorCarta.SEIS),
            cenario("soft 17 com dois ases para", 17, true, false,
                ValorCarta.AS, ValorCarta.AS, ValorCarta.CINCO),
            cenario("17 apos converter o as para um para", 17, false, false,
                ValorCarta.AS, ValorCarta.SEIS, ValorCarta.DEZ),
            cenario("18 para", 18, false, false,
                ValorCarta.DEZ, ValorCarta.OITO),
            cenario("21 para", 21, true, false,
                ValorCarta.AS, ValorCarta.REI),
            cenario("mao estourada nao compra", 22, false, false,
                ValorCarta.REI, ValorCarta.DAMA, ValorCarta.DOIS)
        );
    }

    private static Arguments cenario(
        String descricao,
        int pontuacao,
        boolean macia,
        boolean deveComprar,
        ValorCarta... valores
    ) {
        return Arguments.of(
            descricao,
            List.of(valores),
            pontuacao,
            macia,
            deveComprar
        );
    }

    private static Dealer dealerCom(List<ValorCarta> valores) {
        Dealer dealer = new Dealer();
        Naipe[] naipes = Naipe.values();

        for (int i = 0; i < valores.size(); i++) {
            dealer.getMao().adicionarCarta(
                new Carta(valores.get(i), naipes[i % naipes.length])
            );
        }

        return dealer;
    }
}
