package otavio.machado;

public class Blackjack {
    String nome;
    Baralho baralho = new Baralho();
    Jogador jogador = new Jogador(nome);
    Dealer dealer = new Dealer();

    public Blackjack(Baralho baralho, Jogador jogador, Dealer dealer){
        this.baralho = baralho;
        this.jogador = jogador;
        this.dealer = dealer;
    }

    public static void embaralhar(Baralho baralho){


    }
}
