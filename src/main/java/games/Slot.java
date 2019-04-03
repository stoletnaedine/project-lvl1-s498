package games;

import static java.lang.Math.random;
import static java.lang.Math.round;

public class Slot {

    public static void main(String[] args) {

        int cash = 100;
        int bet = 10;
        int size = 7;

        int firstCounter = 0;
        int secondCounter = 0;
        int thirdCounter = 0;

        while (cash > 0) {
            firstCounter = (firstCounter + randomPush() * 100) % size;
            secondCounter = (secondCounter + randomPush() * 100) % size;
            thirdCounter = (thirdCounter + randomPush() * 100) % size;

            int loss = slotLoss(firstCounter, secondCounter, thirdCounter, bet);

            int result = slotResult(firstCounter, secondCounter, thirdCounter, cash, bet);

            System.out.printf("У Вас %d$, ставка - %d$\n" +
                            "Крутим барабаны!Розыгрыш принёс следующие результаты:\n" +
                            "первый барабан - %d, второй - %d, третий - %d\n" +
                            "Проигрыш %d$, ваш капитал теперь составляет: %d$\n\n",
                    cash, bet, firstCounter, secondCounter, thirdCounter, loss, result);

            cash = result;
        }
    }

    private static int randomPush() {
        return (int) round(random() * 100);
    }

    private static int slotLoss(int firstCounter, int secondCounter, int thirdCounter, int bet) {
        return (firstCounter == secondCounter && firstCounter == thirdCounter) ? 0 : bet;
    }

    private static int slotResult(int firstCounter, int secondCounter, int thirdCounter, int cash, int bet) {
        return (firstCounter == secondCounter && firstCounter == thirdCounter) ? cash + 1000 : cash - bet;
    }

}