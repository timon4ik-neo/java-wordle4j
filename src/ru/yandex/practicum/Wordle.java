package ru.yandex.practicum;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Wordle {
    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final int MAX_ATTEMPTS = 6;

    public static void main(String[] args) throws IOException {

        WordleDictionary dictionary;

        dictionary = new WordleDictionary(DICTIONARY_FILE);

        WordleGame game = new WordleGame(dictionary);


        System.out.println("=== ИГРА WORDLE ===");
        System.out.println("Отгадайте слово из 5 букв.");
        System.out.println("У вас есть " + MAX_ATTEMPTS + " попыток.");

        Scanner scanner = new Scanner(System.in);

        while (!game.isWon() && !game.isLost()) {
            System.out.print("Попытка" + (MAX_ATTEMPTS - game.getStepsLeft() + 1) +
                    " (осталось: " + game.getStepsLeft() + "). Введите слово: ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                String hint = game.getHint();
                System.out.println("Подсказка: " + hint);

                continue;
            }
            System.out.println(game.getAnswer());
            String normalized = input.toLowerCase().replace('ё', 'е');

            if (normalized.length() != 5) {
                System.out.println("должно содержать ровно 5 букв.");
                continue;
            }

            if (!normalized.matches("[а-я]+")) {
                System.out.println("Слово должно содержать только русские буквы.");
                continue;
            }

            if (!dictionary.contains(normalized)) {
                System.out.println("Такого слова нет в словаре.");
                continue;
            }

            String result = game.makeStep(normalized);
            System.out.println("   Результат: " + result);

            if (!game.getHistory().isEmpty()) {
                System.out.println("История: ");
                for (Map.Entry<String, String> entry : game.getHistory().entrySet()) {
                    System.out.println("   " + entry.getKey() + " → " + entry.getValue());
                }
            }
        }

        if (game.isWon()) {
            System.out.println("поздравляем! Вы отгадали слово: " + game.getAnswer());

        } else {
            System.out.println("игра окончена. Загаданное слово: " + game.getAnswer());
        }


    }

}
