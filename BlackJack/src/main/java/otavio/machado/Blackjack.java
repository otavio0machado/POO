package otavio.machado;

import java.util.Optional;

public final class Blackjack {
    private final Baralho baralho;
    private final Jogador jogador;
    private final Dealer dealer;
    private EstadoRodada estado;
    private ResultadoRodada resultado;

    public Blackjack(Baralho baralho, Jogador jogador, Dealer dealer) {
        if (baralho == null) {
            throw new IllegalArgumentException("O baralho não pode ser nulo");
        }

        if (jogador == null) {
            throw new IllegalArgumentException("O jogador não pode ser nulo");
        }

        if (dealer == null) {
            throw new IllegalArgumentException("O dealer não pode ser nulo");
        }

        this.baralho = baralho;
        this.jogador = jogador;
        this.dealer = dealer;
        this.estado = EstadoRodada.CRIADA;
    }

    public void iniciar() {
        validarProntoParaDistribuicao();
        baralho.embaralhar();
        distribuirCartasIniciais();
    }

    public void distribuirCartasIniciais() {
        validarProntoParaDistribuicao();

        jogador.getMao().adicionarCarta(baralho.comprar());
        dealer.getMao().adicionarCarta(baralho.comprar());
        jogador.getMao().adicionarCarta(baralho.comprar());
        dealer.getMao().adicionarCarta(baralho.comprar());

        estado = EstadoRodada.CARTAS_DISTRIBUIDAS;
        avaliarCartasIniciais();
    }

    public Carta pedirCartaJogador() {
        exigirEstado(EstadoRodada.TURNO_DO_JOGADOR);

        Carta cartaComprada = baralho.comprar();
        jogador.getMao().adicionarCarta(cartaComprada);

        if (jogador.getMao().estourou()) {
            finalizar(ResultadoRodada.ESTOURO_JOGADOR);
        } else if (jogador.getMao().temVinteEUm()) {
            estado = EstadoRodada.TURNO_DO_DEALER;
        }

        return cartaComprada;
    }

    public void pararJogador() {
        exigirEstado(EstadoRodada.TURNO_DO_JOGADOR);
        estado = EstadoRodada.TURNO_DO_DEALER;
    }

    public Optional<Carta> avancarTurnoDealer() {
        exigirEstado(EstadoRodada.TURNO_DO_DEALER);

        if (!dealer.deveComprar()) {
            finalizar(determinarResultado());
            return Optional.empty();
        }

        Carta cartaComprada = baralho.comprar();
        dealer.getMao().adicionarCarta(cartaComprada);

        if (dealer.getMao().estourou() || !dealer.deveComprar()) {
            finalizar(determinarResultado());
        }

        return Optional.of(cartaComprada);
    }

    public void executarTurnoDealer() {
        exigirEstado(EstadoRodada.TURNO_DO_DEALER);

        while (estado == EstadoRodada.TURNO_DO_DEALER) {
            avancarTurnoDealer();
        }
    }

    public EstadoRodada getEstado() {
        return estado;
    }

    public ResultadoRodada getResultado() {
        if (estado != EstadoRodada.FINALIZADA) {
            throw new IllegalStateException(
                "O resultado só existe após o fim da rodada"
            );
        }

        return resultado;
    }

    public Baralho getBaralho() {
        return baralho;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public Dealer getDealer() {
        return dealer;
    }

    private void validarProntoParaDistribuicao() {
        exigirEstado(EstadoRodada.CRIADA);

        if (jogador.getMao().getQuantidadeCartas() != 0
            || dealer.getMao().getQuantidadeCartas() != 0) {
            throw new IllegalStateException(
                "As mãos devem estar vazias no início da rodada"
            );
        }

        if (baralho.getQuantidadeCartas() < 4) {
            throw new IllegalStateException(
                "São necessárias ao menos quatro cartas para iniciar"
            );
        }
    }

    private void avaliarCartasIniciais() {
        boolean blackjackJogador = jogador.getMao().temBlackjackNatural();
        boolean blackjackDealer = dealer.getMao().temBlackjackNatural();

        if (blackjackJogador && blackjackDealer) {
            finalizar(ResultadoRodada.EMPATE_COM_BLACKJACK);
        } else if (blackjackJogador) {
            finalizar(ResultadoRodada.BLACKJACK_JOGADOR);
        } else if (blackjackDealer) {
            finalizar(ResultadoRodada.BLACKJACK_DEALER);
        } else {
            estado = EstadoRodada.TURNO_DO_JOGADOR;
        }
    }

    private ResultadoRodada determinarResultado() {
        if (jogador.getMao().estourou()) {
            return ResultadoRodada.ESTOURO_JOGADOR;
        }

        if (dealer.getMao().estourou()) {
            return ResultadoRodada.ESTOURO_DEALER;
        }

        int pontosJogador = jogador.getMao().calcularPontuacao();
        int pontosDealer = dealer.getMao().calcularPontuacao();

        if (pontosJogador > pontosDealer) {
            return ResultadoRodada.VITORIA_JOGADOR;
        }

        if (pontosDealer > pontosJogador) {
            return ResultadoRodada.VITORIA_DEALER;
        }

        return ResultadoRodada.EMPATE;
    }

    private void finalizar(ResultadoRodada resultado) {
        this.resultado = resultado;
        this.estado = EstadoRodada.FINALIZADA;
    }

    private void exigirEstado(EstadoRodada estadoEsperado) {
        if (estado != estadoEsperado) {
            throw new IllegalStateException(
                "Ação inválida no estado " + estado
                    + "; esperado: " + estadoEsperado
            );
        }
    }
}
