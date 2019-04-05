package games;

import java.io.IOException;

import static games.CardUtils.getPar;
import static games.Choice.getCharacterFromUser;

public class BlackJack {

    private static int value(int card) {
        switch (getPar(card)) {
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
    }

    private static int[] cards; // Основная колода
    private static int cursor; // Счётчик карт основной колоды

    private static int[][] playersCards; // карты игроков. Первый индекс - номер игрока
    private static int[] playersCursors; // курсоры карт игроков. Индекс - номер игрока

    private static int[] playersMoney = {100, 100};

    private static final int MAX_VALUE = 21;
    private static final int MAX_CARDS_COUNT = 8;

    private static void initRound() {
        System.out.println("\nУ Вас " + playersMoney[0] + "$, у компьютера - " + playersMoney[1] + "$. Начинаем новый раунд!");
        cards = CardUtils.getShaffledCards();
        playersCards = new int[2][MAX_CARDS_COUNT];
        playersCursors = new int[]{0, 0};
        cursor = 0;
    }

    private static void addCard2Player(int player) {
        playersCards[player][playersCursors[player]] = cards[cursor];
        playersCursors[player]++;
        cards[cursor]++;
    }

    static int sum(int player) {
        int result = 0;
        for (int i = 0; i < playersCursors[player]; i++) {
            result += value(playersCards[player][i]);
        }
        return result;
    }

    static int getFinalSum(int player) {
        return (sum(player) <= MAX_VALUE) ? sum(player) : 0;
    }

    static boolean confirm(String message) throws IOException {
        System.out.println(message + " \"Y\" - Да, {любой другой символ} - нет (Что бы выйти из игры, нажмите Ctrl + C)");
        switch (getCharacterFromUser()) {
            case 'Y': case 'y': return true;
            default: return false;
        }
    }

    static boolean endMoney(int player) {
        return playersMoney[player] == 0;
    }

    static void game(int player) throws IOException {
        addCard2Player(player);
        addCard2Player(player);

        if (sum(player) < 20) {
            for (int i = 0; i < playersCursors[player]; i++) {
                System.out.println("Вам выпала карта " + CardUtils.toString(playersCards[player][i]));
            }
            if (confirm("Берём еще?")) {
                addCard2Player(player);
            }
        }
    }

    public static void main() throws IOException {

            while (!endMoney(0) ||  !endMoney(1)) {

                initRound();
                game(0);
                game(1);
                System.out.printf("Сумма ваших очков - %d, компьютера - %d\n", getFinalSum(0), getFinalSum(1));
                String loss = "Вы проиграли раунд! Теряете ";
                String win = "Вы выиграли раунд! Получаете ";
                System.out.println((getFinalSum(0) < getFinalSum(1)) ? loss : win + "10$");
            }


            if (playersMoney[0] > 0)
                System.out.println("Вы выиграли! Поздравляем!");
            else
                System.out.println("Вы проиграли. Соболезнуем...");
    }
}
