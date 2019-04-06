package games;

import org.slf4j.Logger;

import static games.CardUtils.*;

public class Drunkard {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Drunkard.class);

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

    private static void addCard(int playerIndexTo, int playerIndexFrom, int iteration) {
        if (iteration == 1) {
            playersCardHeads[0] = CARDS_TOTAL_COUNT / 2;
            playersCardHeads[1] = CARDS_TOTAL_COUNT / 2;
        }
        playersCards[playerIndexTo][playersCardHeads[playerIndexTo]] = playersCards[playerIndexFrom][playersCardTails[playerIndexFrom]];
        playersCardTails[playerIndexFrom] = incrementIndex(playersCardTails[playerIndexFrom]);
        playersCardHeads[playerIndexTo] = incrementIndex(playersCardHeads[playerIndexTo]);
    }

    public static void main() {

        int[] cards = getShuffledCards();

        System.arraycopy(cards, 0, playersCards[0], 0, 18);
        System.arraycopy(cards, 18, playersCards[1], 0, 18);

        int iteration = 0;

        boolean gameover = false;

        int winnerComparator = 0;

        while (!gameover){

            iteration++;

            String result;

            int playerOneCard = playersCards[0][playersCardTails[0]] % PARS_TOTAL_COUNT;
            int playerTwoCard = playersCards[1][playersCardTails[1]] % PARS_TOTAL_COUNT;

            if (playerOneCard > playerTwoCard ||
                    (playerOneCard == 0 && playerTwoCard == 8)) {
                result = "Выиграл игрок 1!";
                moveCard(0);
                addCard(0,1, iteration);
                winnerComparator++;
            }
            else
                if (playerOneCard < playerTwoCard ||
                    (playerOneCard == 8 && playerTwoCard == 0)){
                result = "Выиграл игрок 2!";
                moveCard(1);
                addCard(1,0, iteration);
                winnerComparator--;
            }
                else {
                result = "Спор — каждый остаётся при своих!";
                moveCard(0);
                moveCard(1);
                }

            log.info("Итерация №%d: Игрок №1 карта: %s; игрок №2 карта: %s.\n%s\nУ игрока №1 %d карт, у игрока №2 %d карт\n",
                    iteration, CardUtils.toString(playerOneCard), CardUtils.toString(playerTwoCard),
                    result, countCards(0), countCards(1));

            log.info();

            if (playerCardsIsEmpty(0) || playerCardsIsEmpty(1)) {
                gameover = true;
            }
        }

        if (winnerComparator > 0) {
            log.info("Выиграл первый игрок! Количество произведённых итераций: %d.", iteration);
        }

        if (winnerComparator < 0) {
            log.info("Выиграл второй игрок! Количество произведённых итераций: %d.", iteration);
        }
    }
}
