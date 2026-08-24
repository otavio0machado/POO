package otavio.machado;

public class Locomotiva {
    private final String nome;
    private final double potencia;

    public Locomotiva(String nome, double potencia) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da locomotiva é obrigatório.");
        }
        if (!Double.isFinite(potencia) || potencia <= 0) {
            throw new IllegalArgumentException("A potência deve ser maior que zero.");
        }
        this.nome = nome;
        this.potencia = potencia;
    }

    public String getNome() {
        return nome;
    }

    public double getPotencia() {
        return potencia;
    }

    @Override
    public String toString() {
        return String.format("%s — potência: %.2f", nome, potencia);
    }
}
