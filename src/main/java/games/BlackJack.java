package games;

import org.slf4j.Logger;
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

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BlackJack.class);
    private static int[] cards; // Основная колода
    private static int cursor; // Счётчик карт основной колоды
    private static int[][] playersCards; // карты игроков. Первый индекс - номер игрока
    private static int[] playersCursors; // курсоры карт игроков. Индекс - номер игрока
    private static int[] playersMoney = {100, 100}; // стартовые деньги игроков
    private static final int MAX_VALUE = 21; // максимальное значение очков в игре
    private static final int MAX_CARDS_COUNT = 8; // максимальное количество карт у одного игрока
    private static final int BET = 10; // ставка
    private static final int PLAYER = 0; // index игрока
    private static final int AI = 1; // index компьютера
    private static final int PLAYER_STOP_POINT = 20; // количество СТОП-ИГРА очков игрока
    private static final int AI_STOP_POINT = 17; // количество СТОП-ИГРА очков компьютера

    private static void initRound() {
        log.info("\nУ Вас {}$, у компьютера - {}$. Начинаем новый раунд!\n", playersMoney[PLAYER], playersMoney[AI]);
        cards = CardUtils.getShuffledCards();
        playersCards = new int[2][MAX_CARDS_COUNT];
        playersCursors = new int[]{0, 0};
        cursor = 0;
    }

    private static int addCard2Player(int playerIndex) {
        int card = cards[cursor];
        int playerCursor = playersCursors[playerIndex];

        playersCards[playerIndex][playerCursor] = card;
        playersCursors[playerIndex]++;
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

    static int getFinalSum(int playerIndex) {
        return (sum(playerIndex) <= MAX_VALUE) ? sum(playerIndex) : 0;
    }

    static boolean confirm(String message) throws IOException {
        log.info("{} \"Y\" - Да, {любой другой символ} - нет (Что бы выйти из игры, нажмите Ctrl + C)", message);
        switch (getCharacterFromUser()) {
            case 'Y':
            case 'y': return true;
            default: return false;
        }
    }

    static boolean hasMoney(int playerIndex) {
        return playersMoney[playerIndex] != 0;
    }

    static void changeMoney(int winnerPlayer, int loserPlayer) {
        playersMoney[winnerPlayer] += BET;
        playersMoney[loserPlayer] -= BET;
    }

    static String printCard(int playerIndex) {
        return CardUtils.toString(addCard2Player(playerIndex));
    }

    static void game(int playerIndex) throws IOException {
        switch (playerIndex) {
            case 0:  // player
                for (int i = 0; i < 2; i++) {
                    log.info("Вам выпала карта {}", printCard(playerIndex));
                }
                log.info("Сумма моих очков: {}", sum(playerIndex));
                if (sum(playerIndex) >= PLAYER_STOP_POINT)
                    break;
                else
                    while (confirm("Берём еще?")) {
                        log.info("Вам выпала карта {}", printCard(playerIndex));
                        log.info("Сумма моих очков: {}", sum(playerIndex));
                        if (sum(playerIndex) >= PLAYER_STOP_POINT)
                            break;
                    }
                break;
            case 1:  // AI
                for (int i = 0; i < 2; i++) {
                    log.info("Компьютеру выпала карта {}", printCard(playerIndex));
                }
                log.info("Сумма его очков: {}", sum(playerIndex));
                while (sum(playerIndex) <= AI_STOP_POINT) {
                    log.info("Компьютер решил взять ещё и ему выпала карта {}", printCard(playerIndex));
                    log.info("Сумма его очков: {}", sum(playerIndex));
                }
                break;
                default:
                    break;
        }
    }

    private static void printResult(int result) {
        log.info("Сумма ваших очков - {}, компьютера - {}\n", getFinalSum(0), getFinalSum(1));
        switch (result) {
            case 0:
                log.info("Вы выиграли раунд! Получаете {}$\n", BET);
                changeMoney(0, 1);
                break;
            case 1:
                log.info("Вы проиграли раунд! Теряете {}$\n", BET);
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
        while (hasMoney(0) && hasMoney(1)) {

            initRound();

            game(PLAYER);
            game(AI);

            int playerPoints = getFinalSum(0);
            int aiPoints = getFinalSum(1);

            if (playerPoints > aiPoints) printResult(0);
            if (playerPoints < aiPoints) printResult(1);
            if (playerPoints == aiPoints) printResult(2);
        }

        if (playersMoney[0] > 0) {
            log.info("\nВы выиграли! Поздравляем!");
        } else {
            log.info("\nВы проиграли. Соболезнуем...");
        }
    }
}