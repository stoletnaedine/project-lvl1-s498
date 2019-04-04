package games;

import org.apache.commons.math3.util.MathArrays;

import static games.CardUtils.CARDS_TOTAL_COUNT;
import static games.CardUtils.PARS_TOTAL_COUNT;

public class Drunkard {

    private static int[][] playersCards = new int[2][CARDS_TOTAL_COUNT + 1];

    private static int[] playersCardTails = new int[2];
    private static int[] playersCardHeads = new int[2];

    private static int incrementIndex(int i) {
        return (i + 1) % (CARDS_TOTAL_COUNT + 1);
    }

    private static boolean playerCardsIsEmpty(int playerIndex) {
        int tail = playersCardTails[playerIndex];
        int head = playersCardHeads[playerIndex];

        return tail == head;
    }

    private static int countCards(int playerIndex) {
        int countCards = 0;
        int tail = playersCardTails[playerIndex];
        int head = playersCardHeads[playerIndex];

        if (tail > head) {
            countCards = head + CARDS_TOTAL_COUNT - tail + 1;
        }
        if (tail < head) {
            countCards = head - tail;
        }
        return countCards;
    }

    public static void main() {

        int[] cards = new int[CARDS_TOTAL_COUNT];

        for (int i = 0; i < CARDS_TOTAL_COUNT; i++) {
            cards[i] = i;
        }

        MathArrays.shuffle(cards);

        for (int i = 0; i < CARDS_TOTAL_COUNT / 2; i++) {
            playersCards[0][i] = cards[i];
        }

        for (int i = 0; i < CARDS_TOTAL_COUNT / 2; i++) {
            playersCards[1][i] = cards[i + CARDS_TOTAL_COUNT / 2];
        }

        playersCardHeads[0] = CARDS_TOTAL_COUNT / 2;
        playersCardTails[0] = 0;
        playersCardHeads[1] = CARDS_TOTAL_COUNT / 2;
        playersCardTails[1] = 0;

        int iteration = 0;

        boolean winner = false;
        boolean gameover = false;
        String result = null;

        while (!gameover){

            iteration++;

            int movePos0 = playersCardTails[0];
            int movePos1 = playersCardTails[1];

            String cardToString0 = CardUtils.toString(playersCards[0][movePos0]);
            String cardToString1 = CardUtils.toString(playersCards[1][movePos1]);

            int cardValue0 = playersCards[0][movePos0] % PARS_TOTAL_COUNT;
            int cardValue1 = playersCards[1][movePos1] % PARS_TOTAL_COUNT;

            if (cardValue0 > cardValue1 ||
                    (cardValue0 == 0 && cardValue1 == 8)) {
                result = "Выиграл игрок 1!";
                playersCardHeads[0] = incrementIndex(playersCardHeads[0]);
                playersCards[0][playersCardHeads[0]] = playersCards[0][playersCardTails[0]];
                playersCardTails[0] = incrementIndex(playersCardTails[0]);
                playersCardHeads[0] = incrementIndex(playersCardHeads[0]);
                playersCards[0][playersCardHeads[0]] = playersCards[1][playersCardTails[1]];
                playersCardTails[1] = incrementIndex(playersCardTails[1]);
                winner = true;
            }
            else if (cardValue0 < cardValue1 ||
                    (cardValue0 == 8 && cardValue1 == 0)){
                result = "Выиграл игрок 2!";
                playersCardHeads[1] = incrementIndex(playersCardHeads[1]);
                playersCards[1][playersCardHeads[1]] = playersCards[0][playersCardTails[1]];
                playersCardTails[1] = incrementIndex(playersCardTails[1]);
                playersCardHeads[1] = incrementIndex(playersCardHeads[1]);
                playersCards[1][playersCardHeads[1]] = playersCards[0][playersCardTails[0]];
                playersCardTails[0] = incrementIndex(playersCardTails[0]);
                winner = false;
            }
            else if (cardValue0 == cardValue1) {
                result = "Спор — каждый остаётся при своих!";
                playersCardHeads[0] = incrementIndex(playersCardHeads[0]);
                playersCards[0][playersCardHeads[0]] = playersCards[0][playersCardTails[0]];
                playersCardTails[0] = incrementIndex(playersCardTails[0]);

                playersCardHeads[1] = incrementIndex(playersCardHeads[1]);
                playersCards[1][playersCardHeads[1]] = playersCards[1][playersCardTails[1]];
                playersCardTails[1] = incrementIndex(playersCardTails[1]);
            }

            System.out.printf("Итерация №%d: Игрок №1 карта: %s; игрок №2 карта: %s. \n" +
                                result + "\n" +
                                "У игрока №1 %d карт, у игрока №2 %d карт\n",
                        iteration, cardToString0, cardToString1, countCards(0), countCards(1));
            System.out.println();

            if (playerCardsIsEmpty(0) || playerCardsIsEmpty(1)) {
                gameover = true;
            }
        }

        if (winner)
            System.out.printf("Выиграл первый игрок! Количество произведённых итераций: %d.", iteration);

        if (!winner)
            System.out.printf("Выиграл второй игрок! Количество произведённых итераций: %d.", iteration);
    }
}
