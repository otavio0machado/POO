package otavio.machado;

import java.util.Scanner;

public class PatioDeManobras {
    private final Trem[] trens;
    private int quantidade;

    public PatioDeManobras(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade do pátio deve ser maior que zero.");
        }
        trens = new Trem[capacidade];
    }

    public void criarTrem(
            Scanner in,
            GaragemLocomotiva garagemLocomotiva,
            GaragemVagoes garagemVagoes) {
        if (quantidade == trens.length) {
            System.out.println("O pátio de manobras está cheio.");
            return;
        }
        if (garagemLocomotiva.estaVazia()) {
            System.out.println("Não há locomotivas disponíveis para criar um trem.");
            return;
        }
        if (garagemVagoes.estaVazia()) {
            System.out.println("Não há vagões disponíveis para criar um trem.");
            return;
        }

        garagemLocomotiva.conferirGaragem();
        int indiceLocomotiva = lerIndice(
                in,
                "Escolha o número da locomotiva: ",
                garagemLocomotiva.getQuantidade());

        System.out.println();
        garagemVagoes.conferirGaragem();
        int indiceVagao = lerIndice(
                in,
                "Escolha o número do vagão: ",
                garagemVagoes.getQuantidade());

        Locomotiva locomotiva = garagemLocomotiva.retirar(indiceLocomotiva);
        Vagao vagao = garagemVagoes.retirar(indiceVagao);
        trens[quantidade] = new Trem(locomotiva, vagao);
        quantidade++;

        System.out.println("Trem criado e enviado ao pátio de manobras!");
    }

    public void conferirPatio() {
        System.out.println("====== PÁTIO DE MANOBRAS ======");
        if (quantidade == 0) {
            System.out.println("O pátio está vazio.");
            return;
        }

        for (int i = 0; i < quantidade; i++) {
            System.out.printf("Trem %d — %s%n", i + 1, trens[i]);
        }
    }

    public int getQuantidade() {
        return quantidade;
    }

    private int lerIndice(Scanner in, String mensagem, int totalDeOpcoes) {
        while (true) {
            int escolha = Entrada.lerInteiro(in, mensagem);
            if (escolha >= 1 && escolha <= totalDeOpcoes) {
                return escolha - 1;
            }
            System.out.printf("Escolha um número entre 1 e %d.%n", totalDeOpcoes);
        }
    }
}
