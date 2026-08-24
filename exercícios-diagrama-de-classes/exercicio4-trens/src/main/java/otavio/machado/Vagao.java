package otavio.machado;

public class Vagao {
    private final double tamanho;
    private final boolean climatizado;
    private final String nome;

    public Vagao(double tamanho, boolean climatizado, String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do vagão é obrigatório.");
        }
        if (!Double.isFinite(tamanho) || tamanho <= 0) {
            throw new IllegalArgumentException("O tamanho deve ser maior que zero.");
        }
        this.tamanho = tamanho;
        this.climatizado = climatizado;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public double getTamanho() {
        return tamanho;
    }

    public boolean isClimatizado() {
        return climatizado;
    }

    @Override
    public String toString() {
        String possuiClimatizacao = climatizado ? "sim" : "não";
        return String.format("%s — tamanho: %.2f; climatizado: %s", nome, tamanho, possuiClimatizacao);
    }
}
