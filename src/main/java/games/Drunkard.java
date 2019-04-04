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

    private static int[][] playersCards = new int[2][CARDS_TOTAL_COUNT + 1];

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

    private static boolean playerCardsIsEmpty(int playerIndex) {
        int tail = playersCardTails[playerIndex];
        int head = playersCardHeads[playerIndex];

        return tail == head;
    }

    private static int countCards(int playerIndex) {
        int countCards = 0;
        if (playersCardTails[playerIndex] < playersCardHeads[playerIndex]) {
            countCards = CARDS_TOTAL_COUNT - playersCardHeads[playerIndex] + playersCardTails[playerIndex] + 1;
        }
        if (playersCardHeads[playerIndex] == playersCardTails[playerIndex]) {
            countCards = 0;
        }
        else if (playersCardHeads[playerIndex] < playersCardTails[playerIndex]) {
            countCards = playersCardTails[playerIndex] - playersCardHeads[playerIndex] + 1;
        }
        return countCards;
    }

    private static int moveCardPosition(int playerIndex, int prevPosition) {
        int position;
        if (prevPosition == playersCardTails[playerIndex]) {
            position = playersCardHeads[playerIndex];
        }
        else {
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

        for (int i = 0; i < CARDS_TOTAL_COUNT / 2; i++) {
            playersCards[0][i] = cards[i];
        }

        for (int i = 0; i < CARDS_TOTAL_COUNT / 2; i++) {
            playersCards[1][i] = cards[i + CARDS_TOTAL_COUNT / 2];
        }

//        int i = 0;
//        for (int a: cards
//             ) {
//            System.out.print(i + " - ");
//            System.out.println(a);
//            i++;
//        }
//        System.out.println("----------------");
//        System.out.println("player 1");
//
//        int i2 = 0;
//        int[] arr = playersCards[0];
//        for (int b: arr
//        ) {
//            System.out.print(i2 + " - ");
//            System.out.println(b);
//            i2++;
//        }
//        System.out.println("----------------");
//        System.out.println("player 2");
//
//        int i3 = 0;
//        int[] arr2 = playersCards[1];
//        for (int b: arr2
//        ) {
//            System.out.print(i3 + " - ");
//            System.out.println(b);
//            i3++;
//        }
//        System.out.println("----------------");

        playersCardHeads[0] = 17;
        playersCardTails[0] = 0;
        playersCardHeads[1] = 17;
        playersCardTails[1] = 0;

        int brokenCardPosition;
        int newCardPosition;

        int movePos0 = playersCardTails[0];
        int movePos1 = playersCardTails[1];

        int iteration = 1;

        while (!playerCardsIsEmpty(0) || !playerCardsIsEmpty(1) || iteration < 100) {

            String winner = null;

            String cardToString1 = toString(playersCards[0][movePos0]);
            String cardToString2 = toString(playersCards[1][movePos1]);

            int cardValue1 = playersCards[0][movePos0] % PARS_TOTAL_COUNT;
            int cardValue2 = playersCards[1][movePos1] % PARS_TOTAL_COUNT;

            if (cardValue1 > cardValue2 ||
                    (cardValue1 == 0 && cardValue2 == 8)) {
                winner = "Выиграл игрок 1!";
                playersCardTails[0] = incrementIndex(playersCardTails[0]);
                playersCardHeads[1] = incrementIndex(playersCardHeads[1]);
                brokenCardPosition = playersCardHeads[1];
                newCardPosition = incrementIndex(playersCardTails[0]);
                playersCards[0][newCardPosition] = playersCards[1][brokenCardPosition];
            }
            else if (cardValue1 < cardValue2 ||
                    (cardValue1 == 8 && cardValue2 == 0)){
                winner = "Выиграл игрок 2!";
                playersCardTails[1] = incrementIndex(playersCardTails[1]);
                playersCardHeads[0] = incrementIndex(playersCardHeads[0]);
                brokenCardPosition = playersCardHeads[0];
                newCardPosition = incrementIndex(playersCardTails[1]);
                playersCards[1][newCardPosition] = playersCards[0][brokenCardPosition];
            }
            else if (cardValue1 == cardValue2) {
                winner = "Спор — каждый остаётся при своих!";
            }

            System.out.println(cardValue1 + " - " + cardValue2);

                System.out.printf("Итерация №%d: Игрок №1 карта: %s; игрок №2 карта: %s. \n" +
                                winner + "\n" +
                                "У игрока №1 %d карт, у игрока №2 %d карт\n",
                        iteration, cardToString1, cardToString2, countCards(0), countCards(1));
            System.out.println("PLAYER 1: " + playersCardHeads[0] + " | " + playersCardTails[0]);
            System.out.println("PLAYER 2: " + playersCardHeads[1] + " | " + playersCardTails[1]);
            System.out.println("Ход игрока 1: " + movePos0 + "| Ход игрока 2: " + movePos1);
            System.out.println("_____");

            movePos0 = moveCardPosition(0, movePos0);
            movePos1 = moveCardPosition(1, movePos1);
            System.out.println("Ход игрока 1: " + movePos0 + "| Ход игрока 2: " + movePos1);
            System.out.println("_________________");
            iteration++;
        }
        if (playerCardsIsEmpty(1))
            System.out.printf("Выиграл первый игрок! Количество произведённых итераций: %d.", iteration);

        if (playerCardsIsEmpty(0))
            System.out.printf("Выиграл второй игрок! Количество произведённых итераций: %d.", iteration);
    }
}
