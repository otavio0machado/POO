package otavio.machado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void deveCriarBaralhoComCinquentaEDuasCartas() {
        Baralho baralho = new Baralho();

        assertEquals(52, baralho.getQuantidadeCartas());
    }

    @Test
    void deveManterOsDadosDaCarta() {
        Carta carta = new Carta(" q ", Naipe.COPAS);

        assertEquals("Q", carta.getValorNominal());
        assertEquals(Naipe.COPAS, carta.getNaipe());
    }

    @Test
    void deveImpedirCartaSemNaipe() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Carta("A", null)
        );
    }

    @Test
    void deveTratarOpcaoNaoNumericaEPermitirSair() {
        String saida = executarAppComEntrada("texto\n0\n");

        assertTrue(saida.contains("Digite um número entre 0 e 4."));
        assertTrue(saida.contains("Obrigado por visitar"));
    }

    @Test
    void deveInformarQuandoOJogoAindaNaoEstaDisponivel() {
        String saida = executarAppComEntrada("2\n0\n");

        assertTrue(saida.contains("Esse jogo ainda não está disponível."));
    }

    private static String executarAppComEntrada(String texto) {
        PrintStream saidaOriginal = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(bytes, true, StandardCharsets.UTF_8));
            App.executar(new Scanner(texto));
            return bytes.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(saidaOriginal);
        }
    }
}
