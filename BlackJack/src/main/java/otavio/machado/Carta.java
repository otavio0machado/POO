package otavio.machado;

public class Carta {
    private final String valorNominal;
    private final Naipe naipe;

    public Carta(String valorNominal, Naipe naipe) {
        if (valorNominal == null || valorNominal.isBlank()) {
            throw new IllegalArgumentException(
                "O valor nominal não pode ser nulo ou vazio"
            );
        }

        if (naipe == null) {
            throw new IllegalArgumentException("O naipe não pode ser nulo");
        }

        this.valorNominal = valorNominal.trim().toUpperCase();
        this.naipe = naipe;
    }

    public String getValorNominal() {
        return valorNominal;
    }

    public Naipe getNaipe() {
        return naipe;
    }
}
