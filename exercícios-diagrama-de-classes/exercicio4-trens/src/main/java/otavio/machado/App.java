package otavio.machado;

import java.util.Scanner;

public class App {
    private static final int CAPACIDADE_GARAGEM = 1000;
    private static final int CAPACIDADE_PATIO = 1000;

    private static final GaragemLocomotiva garagemLocomotiva =
            new GaragemLocomotiva(new Locomotiva[CAPACIDADE_GARAGEM]);
    private static final GaragemVagoes garagemVagoes =
            new GaragemVagoes(new Vagao[CAPACIDADE_GARAGEM]);
    private static final PatioDeManobras patioDeManobras =
            new PatioDeManobras(CAPACIDADE_PATIO);

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int opcao;

        do {
            exibirMenu();
            opcao = Entrada.lerInteiro(in, "Escolha uma opção: ");
            System.out.println();

            switch (opcao) {
                case 0:
                    System.out.println("Você saiu!");
                    break;
                case 1:
                    garagemLocomotiva.criarLocomotiva(in);
                    break;
                case 2:
                    garagemVagoes.criarVagao(in);
                    break;
                case 3:
                    patioDeManobras.criarTrem(in, garagemLocomotiva, garagemVagoes);
                    break;
                case 4:
                    garagemLocomotiva.conferirGaragem();
                    break;
                case 5:
                    garagemVagoes.conferirGaragem();
                    break;
                case 6:
                    patioDeManobras.conferirPatio();
                    break;
                default:
                    System.out.println("Opção inválida. Escolha um número de 0 a 6.");
            }

            System.out.println();
        } while (opcao != 0);
    }

    private static void exibirMenu() {
        System.out.println("============ MENU ============");
        System.out.println("0) Sair");
        System.out.println("1) Criar locomotiva");
        System.out.println("2) Criar vagão");
        System.out.println("3) Criar trem");
        System.out.println("4) Conferir garagem de locomotivas");
        System.out.println("5) Conferir garagem de vagões");
        System.out.println("6) Conferir pátio de manobras");
        System.out.println("==============================");
    }
}
