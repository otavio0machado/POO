package otavio.machado;

public enum ResultadoRodada {
    BLACKJACK_JOGADOR("Blackjack! O jogador venceu."),
    BLACKJACK_DEALER("Blackjack da banca."),
    EMPATE_COM_BLACKJACK("Jogador e banca fizeram blackjack."),
    VITORIA_JOGADOR("O jogador venceu."),
    VITORIA_DEALER("A banca venceu."),
    EMPATE("A rodada terminou empatada."),
    ESTOURO_JOGADOR("O jogador ultrapassou 21."),
    ESTOURO_DEALER("A banca ultrapassou 21. O jogador venceu.");

    private final String descricao;

    ResultadoRodada(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
