# Blackjack

Projeto de estudo de Java e orientação a objetos. O MVP implementa uma rodada
completa de Blackjack entre um jogador e a banca, com interface textual simples.
O desenho visual das cartas será uma camada separada em uma próxima etapa.

## Regras adotadas

- Um jogador contra uma banca.
- Um baralho padrão de 52 cartas, sem curingas.
- A distribuição inicial alterna jogador, banca, jogador e banca.
- O jogador pode pedir cartas ou parar.
- Ao chegar exatamente a 21, o turno do jogador termina automaticamente.
- A banca compra abaixo de 17 e para em 17 ou mais.
- A banca também para em soft 17.
- Ás vale 11 enquanto isso não fizer a mão estourar; depois passa a valer 1.
- Blackjack natural é 21 com exatamente duas cartas.
- Ambos os participantes com blackjack natural resultam em empate.
- Uma compra em baralho vazio lança `IllegalStateException`.
- Cada nova rodada cria um novo baralho, novas mãos e uma nova instância do jogo.

Ficam fora deste MVP: apostas, split, double down, seguro, surrender, múltiplos
jogadores, múltiplos baralhos e persistência de histórico.

## Organização da lógica

- `Carta` e `ValorCarta` representam cartas válidas e imutáveis.
- `Baralho` cria, embaralha e entrega cartas, removendo cada carta comprada.
- `Mao` conserva as cartas e calcula pontuação, ases, natural e estouro.
- `Jogador` e `Dealer` possuem mãos independentes.
- `Blackjack` coordena a rodada por estados e determina seu resultado.
- `App` cuida apenas da entrada e saída no terminal.

## Requisitos

- Java 25
- Maven

## Executar

```bash
mvn exec:java -Dexec.mainClass="otavio.machado.App"
```

## Testar

```bash
mvn clean test
```

## Compilar e empacotar

```bash
mvn package
```
