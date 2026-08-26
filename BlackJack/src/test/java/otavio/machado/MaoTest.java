package otavio.machado;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class MaoTest {

    @Test
    void deveComecarVazia() {
        Mao mao = new Mao();

        assertAll(
            () -> assertEquals(0, mao.getQuantidadeCartas()),
            () -> assertEquals(0, mao.calcularPontuacao()),
            () -> assertFalse(mao.ehMacia()),
            () -> assertFalse(mao.temVinteEUm()),
            () -> assertFalse(mao.temBlackjackNatural()),
            () -> assertFalse(mao.estourou())
        );
    }

    @Test
    void deveConservarAMesmaCartaAdicionada() {
        Mao mao = new Mao();
        Carta carta = carta(ValorCarta.DAMA, Naipe.COPAS);

        mao.adicionarCarta(carta);

        assertAll(
            () -> assertEquals(1, mao.getQuantidadeCartas()),
            () -> assertSame(carta, mao.getCartas().get(0))
        );
    }

    @Test
    void deveRejeitarCartaNulaSemAlterarAMao() {
        Mao mao = new Mao();

        assertThrows(
            IllegalArgumentException.class,
            () -> mao.adicionarCarta(null)
        );
        assertEquals(0, mao.getQuantidadeCartas());
    }

    @Test
    void deveFornecerSnapshotImutavelDasCartas() {
        Mao mao = new Mao();
        Carta primeira = carta(ValorCarta.AS, Naipe.ESPADAS);
        Carta segunda = carta(ValorCarta.REI, Naipe.COPAS);
        mao.adicionarCarta(primeira);

        List<Carta> snapshot = mao.getCartas();

        assertThrows(
            UnsupportedOperationException.class,
            () -> snapshot.add(segunda)
        );

        mao.adicionarCarta(segunda);

        assertAll(
            () -> assertEquals(1, snapshot.size()),
            () -> assertSame(primeira, snapshot.get(0)),
            () -> assertEquals(2, mao.getCartas().size())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cenariosDePontuacao")
    void deveCalcularPontuacaoEEstadosDerivados(
        String descricao,
        List<ValorCarta> valores,
        int pontuacaoEsperada,
        boolean maciaEsperada,
        boolean naturalEsperado,
        boolean estouroEsperado
    ) {
        Mao mao = maoCom(valores);

        assertAll(
            () -> assertEquals(
                pontuacaoEsperada,
                mao.calcularPontuacao(),
                descricao
            ),
            () -> assertEquals(maciaEsperada, mao.ehMacia(), descricao),
            () -> assertEquals(
                pontuacaoEsperada == 21,
                mao.temVinteEUm(),
                descricao
            ),
            () -> assertEquals(
                naturalEsperado,
                mao.temBlackjackNatural(),
                descricao
            ),
            () -> assertEquals(estouroEsperado, mao.estourou(), descricao),
            () -> assertEquals(valores.size(), mao.getQuantidadeCartas())
        );
    }

    @ParameterizedTest
    @EnumSource(
        value = ValorCarta.class,
        names = {"DEZ", "VALETE", "DAMA", "REI"}
    )
    void asComQualquerCartaDeValorDezDeveSerNatural(
        ValorCarta cartaDeValorDez
    ) {
        Mao asPrimeiro = maoCom(List.of(ValorCarta.AS, cartaDeValorDez));
        Mao asPorUltimo = maoCom(List.of(cartaDeValorDez, ValorCarta.AS));

        assertAll(
            () -> assertTrue(asPrimeiro.temBlackjackNatural()),
            () -> assertTrue(asPorUltimo.temBlackjackNatural())
        );
    }

    @Test
    void deveRecalcularOsValoresDerivadosQuandoRecebeOutraCarta() {
        Mao mao = maoCom(List.of(ValorCarta.AS, ValorCarta.SEIS));

        assertAll(
            () -> assertEquals(17, mao.calcularPontuacao()),
            () -> assertTrue(mao.ehMacia())
        );

        mao.adicionarCarta(carta(ValorCarta.DEZ, Naipe.PAUS));

        assertAll(
            () -> assertEquals(17, mao.calcularPontuacao()),
            () -> assertFalse(mao.ehMacia()),
            () -> assertFalse(mao.estourou()),
            () -> assertEquals(3, mao.getQuantidadeCartas())
        );
    }

    private static Stream<Arguments> cenariosDePontuacao() {
        return Stream.of(
            cenario("10 + Q = 20", 20, false, false, false,
                ValorCarta.DEZ, ValorCarta.DAMA),
            cenario("A + 9 = 20 macio", 20, true, false, false,
                ValorCarta.AS, ValorCarta.NOVE),
            cenario("A + K = blackjack natural", 21, true, true, false,
                ValorCarta.AS, ValorCarta.REI),
            cenario("A + A = 12 macio", 12, true, false, false,
                ValorCarta.AS, ValorCarta.AS),
            cenario("A + A + 9 = 21 macio, mas nao natural", 21, true, false,
                false, ValorCarta.AS, ValorCarta.AS, ValorCarta.NOVE),
            cenario("A + 6 + 10 = 17 duro", 17, false, false, false,
                ValorCarta.AS, ValorCarta.SEIS, ValorCarta.DEZ),
            cenario("A + A + A + 8 = 21 macio", 21, true, false, false,
                ValorCarta.AS, ValorCarta.AS, ValorCarta.AS, ValorCarta.OITO),
            cenario("A + A + A + 9 = 12 duro", 12, false, false, false,
                ValorCarta.AS, ValorCarta.AS, ValorCarta.AS, ValorCarta.NOVE),
            cenario("A + A + 10 + K = 22 e estoura", 22, false, false, true,
                ValorCarta.AS, ValorCarta.AS, ValorCarta.DEZ, ValorCarta.REI),
            cenario("7 + 7 + 7 = 21, mas nao natural", 21, false, false,
                false, ValorCarta.SETE, ValorCarta.SETE, ValorCarta.SETE),
            cenario("K + Q + 2 = 22 e estoura", 22, false, false, true,
                ValorCarta.REI, ValorCarta.DAMA, ValorCarta.DOIS),
            cenario("A + K + Q = 21 duro, mas nao natural", 21, false, false,
                false, ValorCarta.AS, ValorCarta.REI, ValorCarta.DAMA)
        );
    }

    private static Arguments cenario(
        String descricao,
        int pontuacao,
        boolean macia,
        boolean natural,
        boolean estourou,
        ValorCarta... valores
    ) {
        return Arguments.of(
            descricao,
            List.of(valores),
            pontuacao,
            macia,
            natural,
            estourou
        );
    }

    private static Mao maoCom(List<ValorCarta> valores) {
        Mao mao = new Mao();
        Naipe[] naipes = Naipe.values();

        for (int i = 0; i < valores.size(); i++) {
            mao.adicionarCarta(carta(valores.get(i), naipes[i % naipes.length]));
        }

        return mao;
    }

    private static Carta carta(ValorCarta valor, Naipe naipe) {
        return new Carta(valor, naipe);
    }
}
