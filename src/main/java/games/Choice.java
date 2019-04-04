package games;

import java.io.IOException;

import static org.apache.logging.log4j.util.Strings.LINE_SEPARATOR;


public class Choice {

    static char getCharacterFromUser() throws IOException {
        byte[] input = new byte[1 + LINE_SEPARATOR.length()];
        if (System.in.read(input) != input.length)
            throw new RuntimeException("Пользователь ввёл недостаточное кол-во символов");
        return (char) input[0];
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Выберите игру:\n1 - \"однорукий бандит\", 2 - \"пьяница\"");

        switch (getCharacterFromUser()) {
            case '1': Slot.main(); break;
            case '2': Drunkard.main(); break;
            default: System.out.println("Игры с таким номером нет!");
        }
    }
}
