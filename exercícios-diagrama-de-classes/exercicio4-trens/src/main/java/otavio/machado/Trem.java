package otavio.machado;

public class Trem {
    private final Locomotiva locomotiva;
    private final Vagao vagao;

    public Trem(Locomotiva locomotiva, Vagao vagao) {
        if (locomotiva == null || vagao == null) {
            throw new IllegalArgumentException("Um trem precisa de uma locomotiva e de um vagão.");
        }
        this.locomotiva = locomotiva;
        this.vagao = vagao;
    }

    public Locomotiva getLocomotiva() {
        return locomotiva;
    }

    public Vagao getVagao() {
        return vagao;
    }

    @Override
    public String toString() {
        return String.format("locomotiva: %s | vagão: %s", locomotiva.getNome(), vagao.getNome());
    }
}
