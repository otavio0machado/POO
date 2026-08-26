package otavio.machado;

public class Jogador {
    private final String nome;
    private final Mao mao;

    public Jogador(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                "O nome do jogador não pode ser nulo ou vazio"
            );
        }

        this.nome = nome.trim();
        this.mao = new Mao();
    }

    public String getNome() {
        return nome;
    }

    public Mao getMao() {
        return mao;
    }
}
