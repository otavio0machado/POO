package otavio.machado;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

class BaralhoTest {

    @Test
    void deveCriarAsCinquentaEDuasCombinacoesUnicas() {
        List<Carta> cartas = new Baralho().getCartas();
        Set<Carta> cartasUnicas = new HashSet<>(cartas);

        assertAll(
            () -> assertEquals(52, cartas.size()),
            () -> assertEquals(52, cartasUnicas.size()),
            () -> assertTrue(cartas.stream().noneMatch(carta -> carta == null)),
            () -> {
                for (Naipe naipe : Naipe.values()) {
                    long quantidade = cartas.stream()
                        .filter(carta -> carta.getNaipe() == naipe)
                        .count();
                    assertEquals(13, quantidade, "naipe " + naipe);
                }
            },
            () -> {
                for (ValorCarta valor : ValorCarta.values()) {
                    long quantidade = cartas.stream()
                        .filter(carta -> carta.getValor() == valor)
                        .count();
                    assertEquals(4, quantidade, "valor " + valor);
                }
            }
        );
    }

    @Test
    void deveCopiarDefensivamenteAsCartasRecebidas() {
        Carta carta = new Carta(ValorCarta.AS, Naipe.COPAS);
        List<Carta> origem = new ArrayList<>();
        origem.add(carta);

        Baralho baralho = new Baralho(origem);
        origem.clear();

        assertAll(
            () -> assertEquals(1, baralho.getQuantidadeCartas()),
            () -> assertSame(carta, baralho.getCartas().get(0))
        );
    }

    @Test
    void deveRejeitarListaNulaOuComCartaNula() {
        List<Carta> comCartaNula = Arrays.asList(
            new Carta(ValorCarta.AS, Naipe.COPAS),
            null
        );

        assertAll(
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new Baralho(null)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new Baralho(comCartaNula)
            )
        );
    }

    @Test
    void deveDevolverSnapshotImutavelDasCartas() {
        Baralho baralho = new Baralho();
        List<Carta> snapshot = baralho.getCartas();

        assertThrows(
            UnsupportedOperationException.class,
            () -> snapshot.add(new Carta(ValorCarta.AS, Naipe.COPAS))
        );

        baralho.comprar();

        assertAll(
            () -> assertEquals(52, snapshot.size()),
            () -> assertEquals(51, baralho.getCartas().size())
        );
    }

    @Test
    void comprarDeveRemoverEDevolverAMesmaCartaDoTopo() {
        Carta primeira = new Carta(ValorCarta.DOIS, Naipe.COPAS);
        Carta topo = new Carta(ValorCarta.REI, Naipe.ESPADAS);
        Baralho baralho = new Baralho(List.of(primeira, topo));

        Carta comprada = baralho.comprar();

        assertAll(
            () -> assertSame(topo, comprada),
            () -> assertEquals(1, baralho.getQuantidadeCartas()),
            () -> assertSame(primeira, baralho.getCartas().get(0)),
            () -> assertFalse(baralho.estaVazio())
        );
    }

    @Test
    void deveEsgotarSemRepetirERejeitarCompraNoVazio() {
        Baralho baralho = new Baralho();
        Set<Carta> compradas = new HashSet<>();

        while (!baralho.estaVazio()) {
            assertTrue(compradas.add(baralho.comprar()));
        }

        assertAll(
            () -> assertEquals(52, compradas.size()),
            () -> assertEquals(0, baralho.getQuantidadeCartas()),
            () -> assertTrue(baralho.estaVazio()),
            () -> assertThrows(
                IllegalStateException.class,
                baralho::comprar
            )
        );
    }

    @Test
    void embaralharComAMesmaSementeDeveSerDeterministico() {
        Baralho primeiro = new Baralho();
        Baralho segundo = new Baralho();
        List<Carta> conjuntoOriginal = primeiro.getCartas();
        long semente = 20260826L;

        primeiro.embaralhar(new Random(semente));
        segundo.embaralhar(new Random(semente));

        assertAll(
            () -> assertEquals(primeiro.getCartas(), segundo.getCartas()),
            () -> assertEquals(52, primeiro.getQuantidadeCartas()),
            () -> assertEquals(
                new HashSet<>(conjuntoOriginal),
                new HashSet<>(primeiro.getCartas())
            )
        );
    }

    @Test
    void deveRejeitarGeradorAleatorioNulo() {
        Baralho baralho = new Baralho();

        assertThrows(
            IllegalArgumentException.class,
            () -> baralho.embaralhar(null)
        );
    }
}
