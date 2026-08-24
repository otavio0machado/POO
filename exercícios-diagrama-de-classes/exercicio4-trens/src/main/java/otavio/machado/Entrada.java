package otavio.machado;

import java.util.Scanner;

final class Entrada {
    private Entrada() {
    }

    static String lerTexto(Scanner in, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = in.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Digite um texto válido.");
        }
    }

    static int lerInteiro(Scanner in, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = in.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    static double lerDoublePositivo(Scanner in, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = in.nextLine().trim().replace(',', '.');
            try {
                double numero = Double.parseDouble(valor);
                if (Double.isFinite(numero) && numero > 0) {
                    return numero;
                }
            } catch (NumberFormatException e) {
                // A mensagem abaixo também atende ao caso de texto não numérico.
            }
            System.out.println("Digite um número maior que zero.");
        }
    }

    static boolean lerBooleano(Scanner in, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = in.nextLine().trim().toLowerCase();

            if (resposta.equals("sim") || resposta.equals("s") || resposta.equals("true")) {
                return true;
            }
            if (resposta.equals("não") || resposta.equals("nao")
                    || resposta.equals("n") || resposta.equals("false")) {
                return false;
            }
            System.out.println("Responda com sim ou não.");
        }
    }
}
