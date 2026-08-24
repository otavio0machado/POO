package otavio.machado;
import java.util.*;

public class App {
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int opcao = -1;


        while(opcao != 0){
            menu();
            opcao = in.nextInt();
            switch(opcao){
                case 1:
                    System.out.println("Qual seu nome de usuário?");
                    String nome = in.next();
                    playBlackjack(nome);
                    break;
            }
        }
        

       
    }

    public static void menu(){
        System.out.println("========= Bem-vindo ao Cassino Copstein =========");
        System.out.println("Selecione o que você deseja jogar:");
        System.out.println("1) Blackjack");
        System.out.println("2) Poker");
        System.out.println("3) Little Tiger");
        System.out.println("4) Truco");
        System.out.println("=================================================");
    }

    public static void playBlackjack(String nome){
        wellcomeBlackjack();
        Jogador user = new Jogador(nome);
        Baralho baralho = new Baralho();
        Dealer banca = new Dealer();
        Blackjack jogo = new Blackjack(baralho, user, banca);
    }

    public static void wellcomeBlackjack(){
        System.out.println("================ Blackjack ===============");
        System.out.println("|                                        |");
        System.out.println("|      Estamos preparando seu jogo...    |");
        System.out.println("|                                        |");
        System.out.println("==========================================");

    }
    
}
