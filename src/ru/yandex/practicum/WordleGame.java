package ru.yandex.practicum;

import java.util.*;

public class WordleGame {

    private String answer;

    private int steps;

    private WordleDictionary dictionary;

    private boolean isWon;

    private LinkedHashMap<String, String> history;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
        this.steps = 6;
        isWon = false;
        this.history = new LinkedHashMap<>();
    }

    public String makeStep(String guess) {
        if (isWon || steps <= 0) {
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
            if (!matchesMask(word, guessWord, expected)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesMask(String word, String guess, String mask) {
        char[] wordChars = word.toCharArray();
        char[] guessChars = guess.toCharArray();

        for (int i = 0; i < 5; i++) {
            if (mask.charAt(i) == '+') {
                if (wordChars[i] != guessChars[i]) return false;
                wordChars[i] = '\0';
                guessChars[i] = '\0';
            }
        }

        for (int i = 0; i < 5; i++) {
            if (mask.charAt(i) == '^') {
                char c = guessChars[i];
                if (wordChars[i] == c) return false;
                boolean found = false;
                for (int j = 0; j < 5; j++) {
                    if (wordChars[j] == c) {
                        found = true;
                        wordChars[j] = '\0';
                        break;
                    }
                }
                if (!found) return false;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (mask.charAt(i) == '-') {
                char c = guessChars[i];
                boolean alreadyAccounted = false;
                for (int j = 0; j < 5; j++) {
                    if (guess.charAt(j) == c && (mask.charAt(j) == '+' || mask.charAt(j) == '^')) {
                        alreadyAccounted = true;
                        break;
                    }
                }
                if (!alreadyAccounted) {
                    for (int j = 0; j < 5; j++) {
                        if (wordChars[j] == c) return false;
                    }
                }
            }
        }

        return true;
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
                if (targetChars[j] == guessChars[i]) {
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

    public boolean isWon() {
        return isWon;
    }

    public boolean isLost() {
        return steps == 0 && !isWon;
    }

    public int getStepsLeft() {
        return steps;
    }

    public String getAnswer() {
        return answer;
    }

    public LinkedHashMap<String, String> getHistory() {
        return history;
    }

}
