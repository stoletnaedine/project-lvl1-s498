package games;

import static games.CardUtils.*;

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

        if (head < tail) {
            countCards = head + CARDS_TOTAL_COUNT - tail + 1;
        }
        if (head > tail) {
            countCards = head - tail;
        }
        return countCards;
    }

    private static void moveCard(int playerIndex) {
        playersCards[playerIndex][playersCardHeads[playerIndex]] = playersCards[playerIndex][playersCardTails[playerIndex]];
        playersCardTails[playerIndex] = incrementIndex(playersCardTails[playerIndex]);
        playersCardHeads[playerIndex] = incrementIndex(playersCardHeads[playerIndex]);
    }

    private static void addCard(int playerIndexTo, int playerIndexFrom) {
        playersCards[playerIndexTo][playersCardHeads[playerIndexTo]] = playersCards[playerIndexFrom][playersCardTails[playerIndexFrom]];
        playersCardTails[playerIndexFrom] = incrementIndex(playersCardTails[playerIndexFrom]);
        playersCardHeads[playerIndexTo] = incrementIndex(playersCardHeads[playerIndexTo]);
    }

    private static int cardValue(int playerIndex) {
        return playersCards[playerIndex][playersCardTails[playerIndex]] % PARS_TOTAL_COUNT;
    }

    public static void main() {

        int[] cards = getShaffledCards();

        System.arraycopy(cards, 0, playersCards[0], 0, 18);

        System.arraycopy(cards, 18, playersCards[1], 0, 18);

        playersCardHeads[0] = CARDS_TOTAL_COUNT / 2;
        playersCardHeads[1] = CARDS_TOTAL_COUNT / 2;

        int iteration = 0;

        boolean gameover = false;

        int winnerComparator = 0;

        while (!gameover){

            iteration++;

            String result;

            if (cardValue(0) > cardValue(1) ||
                    (cardValue(0) == 0 && cardValue(1) == 8)) {
                result = "Выиграл игрок 1!";
                moveCard(0);
                addCard(0,1);
                winnerComparator++;
            }
            else
                if (cardValue(0) < cardValue(1) ||
                    (cardValue(0) == 8 && cardValue(1) == 0)){
                result = "Выиграл игрок 2!";
                moveCard(1);
                addCard(1,0);
                winnerComparator--;
            }
                else {
                result = "Спор — каждый остаётся при своих!";
                moveCard(0);
                moveCard(1);
                }

            System.out.printf("Итерация №%d: Игрок №1 карта: %s; игрок №2 карта: %s. \n" +
                                result + "\n" +
                                "У игрока №1 %d карт, у игрока №2 %d карт\n",
                        iteration, CardUtils.toString(cardValue(0)), CardUtils.toString(cardValue(1)),
                    countCards(0), countCards(1));

            System.out.println();

            if (playerCardsIsEmpty(0) || playerCardsIsEmpty(1)) {
                gameover = true;
            }
        }

        if (winnerComparator > 0)
            System.out.printf("Выиграл первый игрок! Количество произведённых итераций: %d.", iteration);

        if (winnerComparator < 0)
            System.out.printf("Выиграл второй игрок! Количество произведённых итераций: %d.", iteration);
    }
}
