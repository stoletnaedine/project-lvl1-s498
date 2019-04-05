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
    private static final int bet = 10;
    private static final int PLAYER = 0;
    private static final int AI = 1;
    private static final int playerStopPoint = 20;
    private static final int AIStopPoint = 17;

    private static void initRound() {
        System.out.println("\nУ Вас " + playersMoney[PLAYER] + "$, у компьютера - " + playersMoney[AI] + "$. Начинаем новый раунд!");
        cards = CardUtils.getShuffledCards();
        playersCards = new int[2][MAX_CARDS_COUNT];
        playersCursors = new int[]{0, 0};
        cursor = 0;
    }

    private static int addCard2Player(int player) {
        int card = cards[cursor];
        int playerCursor = playersCursors[player];

        playersCards[player][playerCursor] = card;
        playersCursors[player]++;
        cursor++;
        return card;
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
            case 'Y':
            case 'y': return true;
            default: return false;
        }
    }

    static boolean endMoney(int player) {
        return playersMoney[player] == 0;
    }

    static void changeMoney(int winnerPlayer, int loserPlayer) {
        playersMoney[winnerPlayer] += bet;
        playersMoney[loserPlayer] -= bet;
    }

    static void game(int playerIndex) throws IOException {

        switch (playerIndex) {
            case 0:  // player
                System.out.println("Вам выпала карта " + CardUtils.toString(addCard2Player(playerIndex)));
                System.out.println("Сумма моих очков: " + sum(playerIndex));
                while (confirm("Берём еще?") && sum(playerIndex) < playerStopPoint) {
                    System.out.println("Вам выпала карта " + CardUtils.toString(addCard2Player(playerIndex)));
                    System.out.println("Сумма моих очков: " + sum(playerIndex));
                }
                break;

            case 1:  // AI
                System.out.println("Компьютеру выпала карта " + CardUtils.toString(addCard2Player(playerIndex)));
                System.out.println("Компьютеру выпала карта " + CardUtils.toString(addCard2Player(playerIndex)));
                System.out.println("Сумма его очков: " + sum(playerIndex));
                while (sum(playerIndex) <= AIStopPoint) {
                    System.out.println("Компьютер решил взять ещё и ему выпала карта " + CardUtils.toString(addCard2Player(playerIndex)));
                    System.out.println("Сумма его очков: " + sum(playerIndex));
                }
                break;
                default:
                    break;
        }
    }

    public static void main() throws IOException {

        while (!endMoney(0) ||  !endMoney(1)) {

            initRound();

            game(PLAYER);
            game(AI);

            System.out.printf("Сумма ваших очков - %d, компьютера - %d\n", getFinalSum(0), getFinalSum(1));

            String lossMessage = "Вы проиграли раунд! Теряете ";
            String winMessage = "Вы выиграли раунд! Получаете ";
            boolean playerWin = getFinalSum(0) > getFinalSum(1);

            System.out.println(playerWin ? winMessage : lossMessage + bet + "$");
            if ((playerWin)) {
                changeMoney(0, 1);
            } else {
                changeMoney(1, 0);
            }
        }

        if (playersMoney[0] > 0)
            System.out.println("Вы выиграли! Поздравляем!");
        else
            System.out.println("Вы проиграли. Соболезнуем...");
    }
}
