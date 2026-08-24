package otavio.machado;

public enum Naipe {
    COPAS("♥"),
    OUROS("♦"),
    PAUS("♣"),
    ESPADAS("♠");

    private final String simbolo;

    Naipe(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
