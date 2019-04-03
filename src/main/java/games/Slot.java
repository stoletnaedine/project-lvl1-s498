package games;

import static java.lang.Math.random;
import static java.lang.Math.round;

public class Slot {

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

            System.out.printf("У Вас %d$, ставка - %d$\n" +
                            "Крутим барабаны!Розыгрыш принёс следующие результаты:\n" +
                            "первый барабан - %d, второй - %d, третий - %d\n" +
                            "Проигрыш %d$, ваш капитал теперь составляет: %d$\n\n",
                    cash, bet, firstCounter, secondCounter, thirdCounter, loss, result);

            cash = result;
        }
    }
}