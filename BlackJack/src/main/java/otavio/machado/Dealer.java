package otavio.machado;

public class Dealer {
    private final Mao mao;

    public Dealer() {
        this.mao = new Mao();
    }

    public Mao getMao() {
        return mao;
    }

    public boolean deveComprar() {
        return mao.calcularPontuacao() < 17;
    }
}
