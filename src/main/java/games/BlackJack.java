package games;

import org.slf4j.Logger;

import java.io.IOException;

import static games.CardUtils.getPar;
import static games.Choice.getCharacterFromUser;

public class BlackJack {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BlackJack.class);

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
    private static int[] playersMoney = {100, 100}; // стартовые деньги игроков
    private static final int MAX_VALUE = 21; // максимальное значение очков в игре
    private static final int MAX_CARDS_COUNT = 8; // максимальное количество карт у одного игрока
    private static final int bet = 10; // ставка
    private static final int PLAYER = 0; // id игрока
    private static final int AI = 1; // id компьютера
    private static final int playerStopPoint = 20; // количество СТОП-ИГРА очков игрока
    private static final int AIStopPoint = 17; // количество СТОП-ИГРА очков компьютера

    private static void initRound() {
        log.info("\nУ Вас " + playersMoney[PLAYER] + "$, у компьютера - " + playersMoney[AI] + "$. Начинаем новый раунд!\n");
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
        log.info(message + " \"Y\" - Да, {любой другой символ} - нет (Что бы выйти из игры, нажмите Ctrl + C)");
        switch (getCharacterFromUser()) {
            case 'Y':
            case 'y': return true;
            default: return false;
        }
    }

    static boolean noMoney(int player) {
        return playersMoney[player] == 0;
    }

    static void changeMoney(int winnerPlayer, int loserPlayer) {
        playersMoney[winnerPlayer] += bet;
        playersMoney[loserPlayer] -= bet;
    }

    static void game(int playerIndex) throws IOException {
        switch (playerIndex) {
            case 0:  // player
                log.info("Вам выпала карта " + CardUtils.toString(addCard2Player(playerIndex)));
                do {
                    log.info("Вам выпала карта " + CardUtils.toString(addCard2Player(playerIndex)));
                    log.info("Сумма моих очков: " + sum(playerIndex));
                } while (confirm("Берём еще?") && sum(playerIndex) < playerStopPoint);
                break;
            case 1:  // AI
                int iter = 0;
                while (iter < 2) {
                    log.info("Компьютеру выпала карта " + CardUtils.toString(addCard2Player(playerIndex)));
                    iter++;
                }
                log.info("Сумма его очков: " + sum(playerIndex));
                while (sum(playerIndex) <= AIStopPoint) {
                    log.info("Компьютер решил взять ещё и ему выпала карта " + CardUtils.toString(addCard2Player(playerIndex)));
                    log.info("Сумма его очков: " + sum(playerIndex));
                }
                break;
                default:
                    break;
        }
    }

    private static void printResult(int result) {
        log.info("Сумма ваших очков - %d, компьютера - %d\n", getFinalSum(0), getFinalSum(1));
        switch (result) {
            case 0:
                log.info("Вы выиграли раунд! Получаете %d$\n", bet);
                changeMoney(0, 1);
                break;
            case 1:
                log.info("Вы проиграли раунд! Теряете %d$\n", bet);
                changeMoney(1, 0);
                break;
            case 2:
                log.info("Ничья!\n");
                break;
            default:
                break;
        }
    }

    public static void main() throws IOException {
        while (!noMoney(0) && !noMoney(1)) {

            initRound();

            game(PLAYER);
            game(AI);

            int playerPoints = getFinalSum(0);
            int AIPoints = getFinalSum(1);

            if (playerPoints > AIPoints) {
                printResult(0);
            }
            if (playerPoints < AIPoints) {
                printResult(1);
            }
            if (playerPoints == AIPoints) {
                printResult(2);
            }
        }

        if (playersMoney[0] > 0) {
            log.info("\nВы выиграли! Поздравляем!");
        } else {
            log.info("\nВы проиграли. Соболезнуем...");
        }
    }
}
