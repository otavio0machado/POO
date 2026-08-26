package otavio.machado;

import java.util.ArrayList;
import java.util.List;

public class Mao {
    private final List<Carta> cartas;

    public Mao() {
        this.cartas = new ArrayList<>();
    }

    public void adicionarCarta(Carta carta) {
        if (carta == null) {
            throw new IllegalArgumentException(
                "A carta não pode ser nula"
            );
        }

        cartas.add(carta);
    }

    public int getQuantidadeCartas() {
        return cartas.size();
    }

    public List<Carta> getCartas() {
        return List.copyOf(cartas);
    }

    public int calcularPontuacao() {
        return calcularResumo().total();
    }

    public boolean estourou() {
        return calcularPontuacao() > 21;
    }

    public boolean temVinteEUm() {
        return calcularPontuacao() == 21;
    }

    public boolean temBlackjackNatural() {
        return cartas.size() == 2 && temVinteEUm();
    }

    public boolean ehMacia() {
        return calcularResumo().macia();
    }

    private ResumoPontuacao calcularResumo() {
        int total = 0;
        int asesComoOnze = 0;

        for (Carta carta : cartas) {
            total += carta.getValor().getValorBase();

            if (carta.getValor().ehAs()) {
                asesComoOnze++;
            }
        }

        while (total > 21 && asesComoOnze > 0) {
            total -= 10;
            asesComoOnze--;
        }

        return new ResumoPontuacao(total, asesComoOnze > 0);
    }

    private record ResumoPontuacao(int total, boolean macia) {
    }
}
