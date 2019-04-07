package games;

import org.slf4j.Logger;

import static java.lang.Math.random;
import static java.lang.Math.round;

public class Slot {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Slot.class);

    public static void main() {

        int cash = 100;
        int bet = 10;
        int size = 7;

        int firstCounter = 0;
        int secondCounter = 0;
        int thirdCounter = 0;

        int loss;
        int result;

        while (cash > 0) {

            firstCounter = (firstCounter + (int) round(random() * 100)) % size;
            secondCounter = (secondCounter + (int) round(random() * 100)) % size;
            thirdCounter = (thirdCounter + (int) round(random() * 100)) % size;

            boolean isWin = firstCounter == secondCounter && firstCounter == thirdCounter;

            if (isWin) {
                loss = 0;
                result = cash + 1000;
            }
            else {
                loss = bet;
                result = cash - bet;
            }

            log.info("У Вас {}$, ставка - {}$\nКрутим барабаны!Розыгрыш принёс следующие результаты:\n" +
                            "первый барабан - {}, второй - {}, третий - {}\n" +
                            "Проигрыш {}$, ваш капитал теперь составляет: {}$\n\n",
                    cash, bet, firstCounter, secondCounter, thirdCounter, loss, result);

            cash = result;
        }
    }
}