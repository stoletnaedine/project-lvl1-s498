package games;

import org.apache.commons.math3.util.MathArrays;

public class Drunkard {

    enum Suit {
        SPADES, // пики
        HEARTS, // червы
        CLUBS, // трефы
        DIAMONDS // бубны
    }

    enum Par {
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK, // Валет
        QUEEN, // Дама
        KING, // Король
        ACE // Туз
    }

    private static final int PARS_TOTAL_COUNT = Par.values().length; //9

    private static final int CARDS_TOTAL_COUNT = PARS_TOTAL_COUNT * Suit.values().length; //36

    private static int[][] playersCards = new int[2][CARDS_TOTAL_COUNT];

    private static int[] playersCardTails = new int[2];
    private static int[] playersCardHeads = new int[2];

    private static Suit getSuit(int cardNumber) {
        return Suit.values()[cardNumber / PARS_TOTAL_COUNT];
    }

    private static Par getPar(int cardNumber) {
        return Par.values()[cardNumber % PARS_TOTAL_COUNT];
    }

    private static String toString(int cardNumber) {
        return getPar(cardNumber) + " " + getSuit(cardNumber);
    }

    private static int incrementIndex(int i) {
        return (i + 1) % CARDS_TOTAL_COUNT;
    }

    private static int incrementIndexCardLine(int i, int playerIndex) {
        return (i + 1) % countCards(playerIndex);
    }

    private static boolean playerCardsIsEmpty(int playerIndex) {
        int tail = playersCardTails[playerIndex];
        int head = playersCardHeads[playerIndex];

        return tail == head;
    }

    private static int countCards(int playerIndex) {
        return (playersCardTails[playerIndex] < playersCardHeads[playerIndex]) ?
                CARDS_TOTAL_COUNT - playersCardHeads[playerIndex] + playersCardTails[playerIndex] + 1 :
                playersCardTails[playerIndex] - playersCardHeads[playerIndex] + 1;
    }

    private static int moveCardPosition(int playerIndex, int prevPosition) {
        int position = prevPosition;
        if (playersCardHeads[playerIndex] < playersCardTails[playerIndex]) {
            position = incrementIndexCardLine(prevPosition, playerIndex);
        }
        if (playersCardHeads[playerIndex] > playersCardTails[playerIndex]) {
            position = incrementIndex(prevPosition);
        }
        return position;
    }

    public static void main(String[] args) {

        int[] cards = new int[CARDS_TOTAL_COUNT];

        for (int i = 0; i < CARDS_TOTAL_COUNT; i++) {
            cards[i] = i;
        }

        MathArrays.shuffle(cards);

        for (int i = 0; i < CARDS_TOTAL_COUNT; i++) {
            if (i < CARDS_TOTAL_COUNT / 2) {
                playersCards[0][i] = cards[i];
            }
        }

        for (int i = 0; i < CARDS_TOTAL_COUNT; i++) {
            if (i < CARDS_TOTAL_COUNT / 2) {
                playersCards[1][i] = cards[i + CARDS_TOTAL_COUNT / 2];
            }
        }

        playersCardHeads[0] = 0;
        playersCardTails[0] = 17;
        playersCardHeads[1] = 0;
        playersCardTails[1] = 17;

        int brokenCardPosition;
        int newCardPosition;

        int movePos1 = 0;
        int movePos2 = 0;

        for (int iter = 1; iter < 10; iter++) {

            String winner = null;

            String cardToString1 = toString(playersCards[0][movePos1]);
            String cardToString2 = toString(playersCards[1][movePos2]);

            int cardValue1 = playersCards[0][movePos1] % PARS_TOTAL_COUNT;
            int cardValue2 = playersCards[1][movePos2] % PARS_TOTAL_COUNT;

            if (cardValue1 > cardValue2 ||
                    (cardValue1 == 0 && cardValue2 == 8)) {
                winner = "Выиграл игрок 1!";
                playersCardTails[0] = incrementIndex(playersCardTails[0]);
                playersCardHeads[1] = incrementIndex(playersCardHeads[1]);
                brokenCardPosition = playersCardHeads[1];
                newCardPosition = playersCardTails[0] + 1;
                playersCards[0][newCardPosition] = playersCards[1][brokenCardPosition];
            }
            else if (cardValue1 < cardValue2 ||
                    (cardValue1 == 8 && cardValue2 == 0)){
                winner = "Выиграл игрок 2!";
                playersCardTails[1] = incrementIndex(playersCardTails[1]);
                playersCardHeads[0] = incrementIndex(playersCardHeads[0]);
                brokenCardPosition = playersCardHeads[0];
                newCardPosition = playersCardTails[1] + 1;
                playersCards[1][newCardPosition] = playersCards[0][brokenCardPosition];
            }
            else if (cardValue1 == cardValue2) {
                winner = "Спор — каждый остаётся при своих!";
            }

            System.out.println(cardValue1 + " - " + cardValue2);

                System.out.printf("Итерация №%d: Игрок №1 карта: %s; игрок №2 карта: %s. \n" +
                                winner + "\n" +
                                "У игрока №1 %d карт, у игрока №2 %d карт\n",
                        iter, cardToString1, cardToString2, countCards(0), countCards(1));
            System.out.println("PLAYER 1: " + playersCardHeads[0] + " | " + playersCardTails[0]);
            System.out.println("PLAYER 2: " + playersCardHeads[1] + " | " + playersCardTails[1]);
            System.out.println("_________________");

            movePos1 = moveCardPosition(0, movePos1);
            movePos2 = moveCardPosition(1, movePos2);
        }
    }
}
