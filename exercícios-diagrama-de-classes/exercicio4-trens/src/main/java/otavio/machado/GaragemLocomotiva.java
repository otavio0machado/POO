package otavio.machado;

import java.util.Scanner;

public class GaragemLocomotiva {
    private final Locomotiva[] locomotivas;
    private int quantidade;

    public GaragemLocomotiva(Locomotiva[] locomotivas) {
        if (locomotivas == null || locomotivas.length == 0) {
            throw new IllegalArgumentException("A garagem precisa ter espaço para locomotivas.");
        }
        this.locomotivas = locomotivas;
    }

    public void criarLocomotiva(Scanner in) {
        if (quantidade == locomotivas.length) {
            System.out.println("A garagem de locomotivas está cheia.");
            return;
        }

        String nome = Entrada.lerTexto(in, "Qual o nome da locomotiva? ");
        double potencia = Entrada.lerDoublePositivo(in, "Qual a potência da locomotiva? ");

        locomotivas[quantidade] = new Locomotiva(nome, potencia);
        quantidade++;
        System.out.println("Locomotiva criada com sucesso!");
    }

    public void conferirGaragem() {
        System.out.println("=== GARAGEM DE LOCOMOTIVAS ===");
        if (estaVazia()) {
            System.out.println("A garagem está vazia.");
            return;
        }

        for (int i = 0; i < quantidade; i++) {
            System.out.printf("%d) %s%n", i + 1, locomotivas[i]);
        }
    }

    public boolean estaVazia() {
        return quantidade == 0;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Locomotiva retirar(int indice) {
        validarIndice(indice);
        Locomotiva escolhida = locomotivas[indice];

        int elementosParaMover = quantidade - indice - 1;
        if (elementosParaMover > 0) {
            System.arraycopy(locomotivas, indice + 1, locomotivas, indice, elementosParaMover);
        }

        quantidade--;
        locomotivas[quantidade] = null;
        return escolhida;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= quantidade) {
            throw new IndexOutOfBoundsException("Índice de locomotiva inválido: " + indice);
        }
    }
}
