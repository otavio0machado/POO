package otavio.machado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Baralho {
    private final List<Carta> cartas;

    public Baralho() {
        this.cartas = new ArrayList<>();

        for (Naipe naipe : Naipe.values()) {
            adicionarCartasDoNaipe(naipe);
        }
    }

    public Baralho(List<Carta> cartas) {
        if (cartas == null) {
            throw new IllegalArgumentException(
                "A lista de cartas não pode ser nula"
            );
        }

        if (cartas.stream().anyMatch(carta -> carta == null)) {
            throw new IllegalArgumentException(
                "O baralho não pode conter carta nula"
            );
        }

        this.cartas = new ArrayList<>(cartas);
    }

    public int getQuantidadeCartas() {
        return cartas.size();
    }

    public List<Carta> getCartas() {
        return List.copyOf(cartas);
    }

    public boolean estaVazio() {
        return cartas.isEmpty();
    }

    public void embaralhar() {
        Collections.shuffle(cartas);
    }

    public void embaralhar(Random geradorAleatorio) {
        if (geradorAleatorio == null) {
            throw new IllegalArgumentException(
                "O gerador aleatório não pode ser nulo"
            );
        }

        Collections.shuffle(cartas, geradorAleatorio);
    }

    public Carta comprar() {
        if (estaVazio()) {
            throw new IllegalStateException("Não há cartas no baralho");
        }

        return cartas.remove(cartas.size() - 1);
    }

    private void adicionarCartasDoNaipe(Naipe naipe) {
        for (ValorCarta valor : ValorCarta.values()) {
            cartas.add(new Carta(valor, naipe));
        }
    }
}
