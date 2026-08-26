package otavio.machado;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class BlackjackTest {

    @Test
    void deveRejeitarDependenciasNulas() {
        Baralho baralho = new Baralho(List.of());
        Jogador jogador = new Jogador("Otavio");
        Dealer dealer = new Dealer();

        assertAll(
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new Blackjack(null, jogador, dealer)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new Blackjack(baralho, null, dealer)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new Blackjack(baralho, jogador, null)
            )
        );
    }

    @Test
    void deveComecarCriadaEManterAsDependenciasRecebidas() {
        Baralho baralho = new Baralho(List.of());
        Jogador jogador = new Jogador("Otavio");
        Dealer dealer = new Dealer();

        Blackjack jogo = new Blackjack(baralho, jogador, dealer);

        assertAll(
            () -> assertEquals(EstadoRodada.CRIADA, jogo.getEstado()),
            () -> assertSame(baralho, jogo.getBaralho()),
            () -> assertSame(jogador, jogo.getJogador()),
            () -> assertSame(dealer, jogo.getDealer())
        );
    }

    @Test
    void deveDistribuirQuatroCartasAlternadamenteAPartirDoTopo() {
        Carta primeiraDoJogador = carta("10", Naipe.COPAS);
        Carta primeiraDoDealer = carta("9", Naipe.OUROS);
        Carta segundaDoJogador = carta("7", Naipe.PAUS);
        Carta segundaDoDealer = carta("8", Naipe.ESPADAS);
        Baralho baralho = baralhoComTopo(
            primeiraDoJogador,
            primeiraDoDealer,
            segundaDoJogador,
            segundaDoDealer
        );
        Blackjack jogo = novoJogo(baralho);

        jogo.distribuirCartasIniciais();

        assertAll(
            () -> assertIterableEquals(
                List.of(primeiraDoJogador, segundaDoJogador),
                jogo.getJogador().getMao().getCartas()
            ),
            () -> assertIterableEquals(
                List.of(primeiraDoDealer, segundaDoDealer),
                jogo.getDealer().getMao().getCartas()
            ),
            () -> assertEquals(0, baralho.getQuantidadeCartas()),
            () -> assertEquals(
                EstadoRodada.TURNO_DO_JOGADOR,
                jogo.getEstado()
            )
        );
    }

    @Test
    void naoDeveDistribuirParcialmenteSemQuatroCartas() {
        Baralho baralho = baralhoComTopo(
            carta("2"),
            carta("3"),
            carta("4")
        );
        Blackjack jogo = novoJogo(baralho);
        List<Carta> cartasAntes = baralho.getCartas();

        assertThrows(
            IllegalStateException.class,
            jogo::distribuirCartasIniciais
        );

        assertAll(
            () -> assertIterableEquals(cartasAntes, baralho.getCartas()),
            () -> assertEquals(0, jogo.getJogador().getMao().getQuantidadeCartas()),
            () -> assertEquals(0, jogo.getDealer().getMao().getQuantidadeCartas()),
            () -> assertEquals(EstadoRodada.CRIADA, jogo.getEstado())
        );
    }

    @Test
    void naoDeveConsumirBaralhoQuandoUmaMaoNaoEstaVazia() {
        Baralho baralho = baralhoComTopo(
            carta("2"),
            carta("3"),
            carta("4"),
            carta("5")
        );
        Blackjack jogo = novoJogo(baralho);
        jogo.getJogador().getMao().adicionarCarta(carta("6"));
        List<Carta> cartasAntes = baralho.getCartas();

        assertThrows(
            IllegalStateException.class,
            jogo::distribuirCartasIniciais
        );

        assertAll(
            () -> assertIterableEquals(cartasAntes, baralho.getCartas()),
            () -> assertEquals(1, jogo.getJogador().getMao().getQuantidadeCartas()),
            () -> assertEquals(0, jogo.getDealer().getMao().getQuantidadeCartas()),
            () -> assertEquals(EstadoRodada.CRIADA, jogo.getEstado())
        );
    }

    @Test
    void iniciarDeveDistribuirSomenteUmaVez() {
        Baralho baralho = baralhoComTopo(
            carta("2"),
            carta("3"),
            carta("4"),
            carta("5")
        );
        Blackjack jogo = novoJogo(baralho);

        jogo.iniciar();

        assertAll(
            () -> assertEquals(2, jogo.getJogador().getMao().getQuantidadeCartas()),
            () -> assertEquals(2, jogo.getDealer().getMao().getQuantidadeCartas()),
            () -> assertEquals(0, baralho.getQuantidadeCartas()),
            () -> assertEquals(
                EstadoRodada.TURNO_DO_JOGADOR,
                jogo.getEstado()
            ),
            () -> assertThrows(IllegalStateException.class, jogo::iniciar)
        );
    }

    @Test
    void deveFinalizarQuandoSomenteJogadorTemBlackjackNatural() {
        Blackjack jogo = jogoDistribuido(
            carta("A"),
            carta("9"),
            carta("K"),
            carta("7")
        );

        assertAll(
            () -> assertEquals(EstadoRodada.FINALIZADA, jogo.getEstado()),
            () -> assertEquals(
                ResultadoRodada.BLACKJACK_JOGADOR,
                jogo.getResultado()
            )
        );
    }

    @Test
    void deveFinalizarQuandoSomenteDealerTemBlackjackNatural() {
        Blackjack jogo = jogoDistribuido(
            carta("10"),
            carta("A"),
            carta("9"),
            carta("K")
        );

        assertAll(
            () -> assertEquals(EstadoRodada.FINALIZADA, jogo.getEstado()),
            () -> assertEquals(
                ResultadoRodada.BLACKJACK_DEALER,
                jogo.getResultado()
            )
        );
    }

    @Test
    void deveEmpatarQuandoAmbosTemBlackjackNatural() {
        Blackjack jogo = jogoDistribuido(
            carta("A"),
            carta("A"),
            carta("K"),
            carta("Q")
        );

        assertAll(
            () -> assertEquals(EstadoRodada.FINALIZADA, jogo.getEstado()),
            () -> assertEquals(
                ResultadoRodada.EMPATE_COM_BLACKJACK,
                jogo.getResultado()
            )
        );
    }

    @Test
    void pedirCartaDeveEntregarOTopoEManterTurnoAbaixoDeVinteEUm() {
        Carta cartaPedida = carta("4", Naipe.ESPADAS);
        Baralho baralho = baralhoComTopo(
            carta("5"),
            carta("9"),
            carta("6"),
            carta("7"),
            cartaPedida
        );
        Blackjack jogo = novoJogo(baralho);
        jogo.distribuirCartasIniciais();

        Carta comprada = jogo.pedirCartaJogador();

        assertAll(
            () -> assertSame(cartaPedida, comprada),
            () -> assertEquals(15, jogo.getJogador().getMao().calcularPontuacao()),
            () -> assertEquals(3, jogo.getJogador().getMao().getQuantidadeCartas()),
            () -> assertEquals(
                EstadoRodada.TURNO_DO_JOGADOR,
                jogo.getEstado()
            ),
            () -> assertTrue(baralho.estaVazio())
        );
    }

    @Test
    void deveFinalizarImediatamenteQuandoJogadorEstoura() {
        Blackjack jogo = jogoDistribuido(
            carta("K"),
            carta("9"),
            carta("Q"),
            carta("7"),
            carta("2")
        );

        jogo.pedirCartaJogador();

        assertAll(
            () -> assertTrue(jogo.getJogador().getMao().estourou()),
            () -> assertEquals(EstadoRodada.FINALIZADA, jogo.getEstado()),
            () -> assertEquals(
                ResultadoRodada.ESTOURO_JOGADOR,
                jogo.getResultado()
            )
        );
    }

    @Test
    void devePassarAutomaticamenteAoDealerQuandoJogadorChegaAVinteEUm() {
        Blackjack jogo = jogoDistribuido(
            carta("10"),
            carta("9"),
            carta("5"),
            carta("7"),
            carta("6")
        );

        jogo.pedirCartaJogador();

        assertAll(
            () -> assertEquals(21, jogo.getJogador().getMao().calcularPontuacao()),
            () -> assertFalse(jogo.getJogador().getMao().temBlackjackNatural()),
            () -> assertEquals(
                EstadoRodada.TURNO_DO_DEALER,
                jogo.getEstado()
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::pedirCartaJogador
            )
        );
    }

    @Test
    void pararJogadorDeveApenasTransferirOTurno() {
        Baralho baralho = baralhoComTopo(
            carta("10"),
            carta("9"),
            carta("7"),
            carta("8"),
            carta("2")
        );
        Blackjack jogo = novoJogo(baralho);
        jogo.distribuirCartasIniciais();

        jogo.pararJogador();

        assertAll(
            () -> assertEquals(
                EstadoRodada.TURNO_DO_DEALER,
                jogo.getEstado()
            ),
            () -> assertEquals(2, jogo.getDealer().getMao().getQuantidadeCartas()),
            () -> assertEquals(1, baralho.getQuantidadeCartas()),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::getResultado
            )
        );
    }

    @Test
    void deveExecutarTurnoDoDealerPassoAPasso() {
        Carta primeiraCompra = carta("2", Naipe.PAUS);
        Carta segundaCompra = carta("3", Naipe.OUROS);
        Carta terceiraCompra = carta("A", Naipe.ESPADAS);
        Blackjack jogo = jogoDistribuido(
            carta("10"),
            carta("5"),
            carta("8"),
            carta("6"),
            primeiraCompra,
            segundaCompra,
            terceiraCompra
        );
        jogo.pararJogador();

        Optional<Carta> primeira = jogo.avancarTurnoDealer();

        assertAll(
            () -> assertEquals(Optional.of(primeiraCompra), primeira),
            () -> assertEquals(13, jogo.getDealer().getMao().calcularPontuacao()),
            () -> assertEquals(
                EstadoRodada.TURNO_DO_DEALER,
                jogo.getEstado()
            )
        );

        Optional<Carta> segunda = jogo.avancarTurnoDealer();

        assertAll(
            () -> assertEquals(Optional.of(segundaCompra), segunda),
            () -> assertEquals(16, jogo.getDealer().getMao().calcularPontuacao()),
            () -> assertEquals(
                EstadoRodada.TURNO_DO_DEALER,
                jogo.getEstado()
            )
        );

        Optional<Carta> terceira = jogo.avancarTurnoDealer();

        assertAll(
            () -> assertEquals(Optional.of(terceiraCompra), terceira),
            () -> assertEquals(17, jogo.getDealer().getMao().calcularPontuacao()),
            () -> assertEquals(EstadoRodada.FINALIZADA, jogo.getEstado()),
            () -> assertEquals(
                ResultadoRodada.VITORIA_JOGADOR,
                jogo.getResultado()
            )
        );
    }

    @Test
    void deveExecutarTurnoCompletoDoDealerAteEleParar() {
        Blackjack jogo = jogoDistribuido(
            carta("10"),
            carta("5"),
            carta("8"),
            carta("7"),
            carta("2"),
            carta("5")
        );
        jogo.pararJogador();

        jogo.executarTurnoDealer();

        assertAll(
            () -> assertEquals(19, jogo.getDealer().getMao().calcularPontuacao()),
            () -> assertEquals(4, jogo.getDealer().getMao().getQuantidadeCartas()),
            () -> assertEquals(EstadoRodada.FINALIZADA, jogo.getEstado()),
            () -> assertEquals(
                ResultadoRodada.VITORIA_DEALER,
                jogo.getResultado()
            )
        );
    }

    @Test
    void dealerDevePararEmDezesseteMacioSemComprar() {
        Baralho baralho = baralhoComTopo(
            carta("10"),
            carta("A"),
            carta("8"),
            carta("6"),
            carta("K")
        );
        Blackjack jogo = novoJogo(baralho);
        jogo.distribuirCartasIniciais();
        assertTrue(jogo.getDealer().getMao().ehMacia());
        jogo.pararJogador();

        Optional<Carta> compra = jogo.avancarTurnoDealer();

        assertAll(
            () -> assertEquals(Optional.empty(), compra),
            () -> assertEquals(17, jogo.getDealer().getMao().calcularPontuacao()),
            () -> assertEquals(2, jogo.getDealer().getMao().getQuantidadeCartas()),
            () -> assertEquals(1, baralho.getQuantidadeCartas()),
            () -> assertEquals(EstadoRodada.FINALIZADA, jogo.getEstado()),
            () -> assertEquals(
                ResultadoRodada.VITORIA_JOGADOR,
                jogo.getResultado()
            )
        );
    }

    @Test
    void deveFinalizarComEstouroDoDealer() {
        Blackjack jogo = jogoDistribuido(
            carta("10"),
            carta("10"),
            carta("7"),
            carta("6"),
            carta("K")
        );
        jogo.pararJogador();

        jogo.executarTurnoDealer();

        assertAll(
            () -> assertTrue(jogo.getDealer().getMao().estourou()),
            () -> assertEquals(EstadoRodada.FINALIZADA, jogo.getEstado()),
            () -> assertEquals(
                ResultadoRodada.ESTOURO_DEALER,
                jogo.getResultado()
            )
        );
    }

    @Test
    void deveEmpatarPontuacoesIguaisSemBlackjack() {
        Blackjack jogo = jogoDistribuido(
            carta("10"),
            carta("10"),
            carta("8"),
            carta("8")
        );
        jogo.pararJogador();

        Optional<Carta> compra = jogo.avancarTurnoDealer();

        assertAll(
            () -> assertEquals(Optional.empty(), compra),
            () -> assertEquals(EstadoRodada.FINALIZADA, jogo.getEstado()),
            () -> assertEquals(ResultadoRodada.EMPATE, jogo.getResultado())
        );
    }

    @Test
    void deveRejeitarAcoesDeTurnoAntesDaDistribuicao() {
        Blackjack jogo = novoJogo(baralhoComTopo(
            carta("2"),
            carta("3"),
            carta("4"),
            carta("5")
        ));

        assertAll(
            () -> assertThrows(
                IllegalStateException.class,
                jogo::pedirCartaJogador
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::pararJogador
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::avancarTurnoDealer
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::executarTurnoDealer
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::getResultado
            ),
            () -> assertEquals(EstadoRodada.CRIADA, jogo.getEstado())
        );
    }

    @Test
    void deveRejeitarAcoesDoJogadorDuranteTurnoDoDealer() {
        Blackjack jogo = jogoDistribuido(
            carta("10"),
            carta("9"),
            carta("7"),
            carta("7")
        );
        jogo.pararJogador();

        assertAll(
            () -> assertThrows(
                IllegalStateException.class,
                jogo::pedirCartaJogador
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::pararJogador
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::getResultado
            ),
            () -> assertEquals(
                EstadoRodada.TURNO_DO_DEALER,
                jogo.getEstado()
            )
        );
    }

    @Test
    void deveRejeitarNovasAcoesDepoisDaFinalizacao() {
        Blackjack jogo = jogoDistribuido(
            carta("A"),
            carta("9"),
            carta("K"),
            carta("7")
        );

        assertAll(
            () -> assertThrows(IllegalStateException.class, jogo::iniciar),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::distribuirCartasIniciais
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::pedirCartaJogador
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::pararJogador
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::avancarTurnoDealer
            ),
            () -> assertThrows(
                IllegalStateException.class,
                jogo::executarTurnoDealer
            ),
            () -> assertEquals(
                ResultadoRodada.BLACKJACK_JOGADOR,
                jogo.getResultado()
            )
        );
    }

    private static Blackjack novoJogo(Baralho baralho) {
        return new Blackjack(baralho, new Jogador("Otavio"), new Dealer());
    }

    private static Blackjack jogoDistribuido(Carta... cartasNoTopo) {
        Blackjack jogo = novoJogo(baralhoComTopo(cartasNoTopo));
        jogo.distribuirCartasIniciais();
        return jogo;
    }

    private static Baralho baralhoComTopo(Carta... cartasNoTopo) {
        List<Carta> cartasNaOrdemInterna = new ArrayList<>(
            Arrays.asList(cartasNoTopo)
        );
        Collections.reverse(cartasNaOrdemInterna);
        return new Baralho(cartasNaOrdemInterna);
    }

    private static Carta carta(String valor) {
        return carta(valor, Naipe.COPAS);
    }

    private static Carta carta(String valor, Naipe naipe) {
        return new Carta(valor, naipe);
    }
}
