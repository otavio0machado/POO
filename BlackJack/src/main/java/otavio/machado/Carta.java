package otavio.machado;

import java.util.Objects;

public final class Carta {
    private final ValorCarta valor;
    private final Naipe naipe;

    public Carta(ValorCarta valor, Naipe naipe) {
        if (valor == null) {
            throw new IllegalArgumentException("O valor não pode ser nulo");
        }

        if (naipe == null) {
            throw new IllegalArgumentException("O naipe não pode ser nulo");
        }

        this.valor = valor;
        this.naipe = naipe;
    }

    public Carta(String valorNominal, Naipe naipe) {
        this(ValorCarta.deSimbolo(valorNominal), naipe);
    }

    public ValorCarta getValor() {
        return valor;
    }

    public String getValorNominal() {
        return valor.getSimbolo();
    }

    public Naipe getNaipe() {
        return naipe;
    }

    @Override
    public String toString() {
        return valor.getSimbolo() + naipe.getSimbolo();
    }

    @Override
    public boolean equals(Object outroObjeto) {
        if (this == outroObjeto) {
            return true;
        }

        if (!(outroObjeto instanceof Carta outraCarta)) {
            return false;
        }

        return valor == outraCarta.valor && naipe == outraCarta.naipe;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor, naipe);
    }
}
