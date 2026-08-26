package otavio.machado;

import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        executar(entrada);
    }

    static void executar(Scanner entrada) {
        boolean executando = true;

        while (executando) {
            menu();
            Integer opcao = lerOpcao(entrada, 0, 4);

            if (opcao == null) {
                break;
            }

            switch (opcao) {
                case 0 -> executando = false;
                case 1 -> {
                    String nome = lerNome(entrada);

                    if (nome == null) {
                        executando = false;
                    } else {
                        playBlackjack(nome, entrada);
                    }
                }
                case 2, 3, 4 -> System.out.println(
                    "Esse jogo ainda não está disponível.\n"
                );
                default -> throw new IllegalStateException(
                    "Opção validada fora do intervalo"
                );
            }
        }

        System.out.println("Obrigado por visitar o Cassino Copstein!");
    }

    public static void menu() {
        System.out.println("========= Bem-vindo ao Cassino Copstein =========");
        System.out.println("Selecione o que você deseja jogar:");
        System.out.println("1) Blackjack");
        System.out.println("2) Poker");
        System.out.println("3) Little Tiger");
        System.out.println("4) Truco");
        System.out.println("0) Sair");
        System.out.println("=================================================");
    }

    public static void playBlackjack(String nome) {
        playBlackjack(nome, new Scanner(System.in));
    }

    static void playBlackjack(String nome, Scanner entrada) {
        boolean jogarNovamente = true;

        while (jogarNovamente) {
            Baralho baralho = new Baralho();
            Jogador jogador = new Jogador(nome);
            Dealer dealer = new Dealer();
            Blackjack jogo = new Blackjack(baralho, jogador, dealer);

            jogo.iniciar();
            conduzirRodada(jogo, entrada);

            if (!entrada.hasNextLine()) {
                break;
            }

            jogarNovamente = lerSimNao(
                entrada,
                "Deseja jogar outra rodada? (s/n): "
            );
        }
    }

    private static void conduzirRodada(Blackjack jogo, Scanner entrada) {
        while (jogo.getEstado() == EstadoRodada.TURNO_DO_JOGADOR) {
            mostrarSituacao(jogo, false);
            System.out.println("1) Pedir carta");
            System.out.println("2) Parar");

            Integer acao = lerOpcao(entrada, 1, 2);

            if (acao == null) {
                return;
            }

            if (acao == 1) {
                Carta carta = jogo.pedirCartaJogador();
                System.out.println("Você recebeu: " + carta);
            } else {
                jogo.pararJogador();
            }
        }

        while (jogo.getEstado() == EstadoRodada.TURNO_DO_DEALER) {
            jogo.avancarTurnoDealer().ifPresent(
                carta -> System.out.println("A banca recebeu: " + carta)
            );
        }

        if (jogo.getEstado() == EstadoRodada.FINALIZADA) {
            mostrarSituacao(jogo, true);
            System.out.println(jogo.getResultado().getDescricao());
        }
    }

    private static void mostrarSituacao(
        Blackjack jogo,
        boolean revelarDealer
    ) {
        Mao maoDealer = jogo.getDealer().getMao();
        Mao maoJogador = jogo.getJogador().getMao();

        System.out.println();

        if (revelarDealer) {
            System.out.println(
                "Banca: " + maoDealer.getCartas()
                    + " | Pontos: " + maoDealer.calcularPontuacao()
            );
        } else {
            System.out.println(
                "Banca: " + formatarMaoOculta(maoDealer.getCartas())
            );
        }

        System.out.println(
            jogo.getJogador().getNome() + ": " + maoJogador.getCartas()
                + " | Pontos: " + maoJogador.calcularPontuacao()
        );
        System.out.println();
    }

    private static String formatarMaoOculta(List<Carta> cartas) {
        if (cartas.isEmpty()) {
            return "[]";
        }

        if (cartas.size() == 1) {
            return "[" + cartas.get(0) + "]";
        }

        return "[" + cartas.get(0) + ", ??]";
    }

    private static String lerNome(Scanner entrada) {
        while (true) {
            System.out.print("Qual seu nome de usuário? ");

            if (!entrada.hasNextLine()) {
                return null;
            }

            String nome = entrada.nextLine().trim();

            if (!nome.isEmpty()) {
                return nome;
            }

            System.out.println("O nome não pode ficar vazio.");
        }

    }

    private static Integer lerOpcao(
        Scanner entrada,
        int menorOpcao,
        int maiorOpcao
    ) {
        while (true) {
            System.out.print("Escolha: ");

            if (!entrada.hasNextLine()) {
                return null;
            }

            String texto = entrada.nextLine().trim();

            try {
                int opcao = Integer.parseInt(texto);

                if (opcao >= menorOpcao && opcao <= maiorOpcao) {
                    return opcao;
                }
            } catch (NumberFormatException ignored) {
                // A mensagem abaixo também atende entradas que não são números.
            }

            System.out.printf(
                "Digite um número entre %d e %d.%n",
                menorOpcao,
                maiorOpcao
            );
        }

    }

    private static boolean lerSimNao(Scanner entrada, String mensagem) {
        while (true) {
            System.out.print(mensagem);

            if (!entrada.hasNextLine()) {
                return false;
            }

            String resposta = entrada.nextLine().trim().toLowerCase();

            if (resposta.equals("s") || resposta.equals("sim")) {
                return true;
            }

            if (resposta.equals("n") || resposta.equals("nao")
                || resposta.equals("não")) {
                return false;
            }

            System.out.println("Responda com 's' ou 'n'.");
        }

    }
}
