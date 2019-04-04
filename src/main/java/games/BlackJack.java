package games;

public class BlackJack {

    private static int value(int card) {
        switch (CardUtils.getPar(card)) {
            case JACK: return 2;
            case QUEEN: return 3;
            case KING: return 4;
            case SIX: return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE: return 9;
            case TEN: return 10;
            case ACE:
            default: return 11;
        }

        private static int[] cards; // Основная колода
        private static int cursor; // Счётчик карт основной колоды

        private static int[][] playersCards; // карты игроков. Первый индекс - номер игрока
        private static int[] playersCursors; // курсоры карт игроков. Индекс - номер игрока
    }
}
