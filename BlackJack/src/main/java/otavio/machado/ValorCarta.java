package otavio.machado;

import java.util.Locale;

public enum ValorCarta {
    AS("A", 11),
    DOIS("2", 2),
    TRES("3", 3),
    QUATRO("4", 4),
    CINCO("5", 5),
    SEIS("6", 6),
    SETE("7", 7),
    OITO("8", 8),
    NOVE("9", 9),
    DEZ("10", 10),
    VALETE("J", 10),
    DAMA("Q", 10),
    REI("K", 10);

    private final String simbolo;
    private final int valorBase;

    ValorCarta(String simbolo, int valorBase) {
        this.simbolo = simbolo;
        this.valorBase = valorBase;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public int getValorBase() {
        return valorBase;
    }

    public boolean ehAs() {
        return this == AS;
    }

    public static ValorCarta deSimbolo(String simbolo) {
        if (simbolo == null || simbolo.isBlank()) {
            throw new IllegalArgumentException(
                "O valor da carta não pode ser nulo ou vazio"
            );
        }

        String valorNormalizado = simbolo.trim().toUpperCase(Locale.ROOT);

        for (ValorCarta valor : values()) {
            if (valor.simbolo.equals(valorNormalizado)) {
                return valor;
            }
        }

        throw new IllegalArgumentException(
            "Valor de carta inválido: " + simbolo
        );
    }
}
