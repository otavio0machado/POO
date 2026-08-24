package otavio.machado;

import java.util.Scanner;

public class GaragemVagoes {
    private final Vagao[] vagoes;
    private int quantidade;

    public GaragemVagoes(Vagao[] vagoes) {
        if (vagoes == null || vagoes.length == 0) {
            throw new IllegalArgumentException("A garagem precisa ter espaço para vagões.");
        }
        this.vagoes = vagoes;
    }

    public void criarVagao(Scanner in) {
        if (quantidade == vagoes.length) {
            System.out.println("A garagem de vagões está cheia.");
            return;
        }

        String nome = Entrada.lerTexto(in, "Qual o nome do vagão? ");
        double tamanho = Entrada.lerDoublePositivo(in, "Qual o tamanho do vagão? ");
        boolean climatizado = Entrada.lerBooleano(in, "O ambiente do vagão é climatizado? (sim/não) ");

        vagoes[quantidade] = new Vagao(tamanho, climatizado, nome);
        quantidade++;
        System.out.println("Vagão criado com sucesso!");
    }

    public void conferirGaragem() {
        System.out.println("====== GARAGEM DE VAGÕES ======");
        if (estaVazia()) {
            System.out.println("A garagem está vazia.");
            return;
        }

        for (int i = 0; i < quantidade; i++) {
            System.out.printf("%d) %s%n", i + 1, vagoes[i]);
        }
    }

    public boolean estaVazia() {
        return quantidade == 0;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Vagao retirar(int indice) {
        validarIndice(indice);
        Vagao escolhido = vagoes[indice];

        int elementosParaMover = quantidade - indice - 1;
        if (elementosParaMover > 0) {
            System.arraycopy(vagoes, indice + 1, vagoes, indice, elementosParaMover);
        }

        quantidade--;
        vagoes[quantidade] = null;
        return escolhido;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= quantidade) {
            throw new IndexOutOfBoundsException("Índice de vagão inválido: " + indice);
        }
    }
}
