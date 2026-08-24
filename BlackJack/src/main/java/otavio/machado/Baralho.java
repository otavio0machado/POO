package otavio.machado;

import java.util.ArrayList;
import java.util.List;

public class Baralho {
    private final List<Carta> cartas;

    public Baralho() {
        this.cartas = new ArrayList<>();

        adicionarCartasDoNaipe(Naipe.COPAS);
        adicionarCartasDoNaipe(Naipe.OUROS);
        adicionarCartasDoNaipe(Naipe.PAUS);
        adicionarCartasDoNaipe(Naipe.ESPADAS);
    }

    public int getQuantidadeCartas() {
        return cartas.size();
    }

    private void adicionarCartasDoNaipe(Naipe naipe) {
        String[] valoresNominais = {
            "A", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "J", "Q", "K"
        };

        for (int i = 0; i < valoresNominais.length; i++) {
            String valorNominal = valoresNominais[i];
            Carta carta = new Carta(valorNominal, naipe);
            this.cartas.add(carta);
        }
    }
}
