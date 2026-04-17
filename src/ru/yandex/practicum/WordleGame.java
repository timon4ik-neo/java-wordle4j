package ru.yandex.practicum;

import java.util.*;

public class WordleGame {

    private String answer;

    private int steps;

    private WordleDictionary dictionary;

    private boolean isWon;

    LinkedHashMap<String, String> history;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
        this.steps = 6;
        isWon = false;
        this.history = new LinkedHashMap<>();
    }

    public String makeStep(String guess) {
        if (isWon || steps <= 0 ) {
            throw new RuntimeException("Игра окончена");

        }
        String normalGuess = guess.toLowerCase().replace('ё', 'е');
        String resultMask = checkWorld(normalGuess);
        history.put(normalGuess, resultMask);
        steps--;
        if (normalGuess.equals(answer)) {
            isWon = true;
        }
        return resultMask;
        
    }

    public String getHint() {
        List<String> hint = new ArrayList<>();
        for (String word : dictionary.getWords()) {
            if (matchHistory(word)) {
                hint.add(word);
            }
        }
        if (hint.isEmpty()) {
           return "Подсказок нет";
        }
        Random random = new Random();
        return hint.get(random.nextInt(hint.size()));
    }
    private boolean matchHistory(String word) {
        for (Map.Entry<String, String> entry : history.entrySet()) {
          String guessWord = entry.getKey();
          String expected = entry.getValue();
          if (!machMask(word, guessWord, expected)) {
              return false;
          }
        }
        return true;
    }
    private boolean machMask(String word, String guess, String mask) {

    }

    private String checkWorld(String guess) {
        char[] mask = new char[5];
        Arrays.fill(mask, '-');
        char[] targetChars = answer.toCharArray();
        char[] guessChars = guess.toCharArray();
        for (int i = 0; i < 5; i++) {
            if (guessChars[i] == targetChars[i]) {
                mask[i] = '+';
                targetChars[i] = '\0';
                guessChars[i] = '\0';

            }
        }
        for (int i = 0; i < 5; i++) {
            if (guessChars[i] == '\0') {
                continue;
            }
            boolean found = false;

            for (int j = 0; j < 5; j++) {
                if (targetChars[i] == guessChars[i]) {
                    mask[i] = '^';
                    targetChars[j] = '\0';
                    found = true;
                    break;
                }
            }
            if (!found) {
                mask[i] = '-';

            }
        }
        return new String(mask);
    }
}
