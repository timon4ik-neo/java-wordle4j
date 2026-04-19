package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
class WordleTest {

    private WordleDictionary createDictionary(List<String> wordsList) throws IOException {
        return new WordleDictionary("words_ru.txt") {
            private final List<String> words = wordsList;
            private final Random random = new Random(42);

            @Override
            public String getRandomWord() {
                return words.get(random.nextInt(words.size()));
            }

            @Override
            public boolean contains(String word) {
                return words.contains(word);
            }

            @Override
            public List<String> getWords() {
                return words;
            }

            @Override
            public int size() {
                return words.size();
            }
        };
    }

    @Test
    void testExactMatchWinsGame() throws IOException {
        WordleDictionary dictionary = createDictionary(Arrays.asList("слово", "книга", "трава"));
        WordleGame game = new WordleGame(dictionary);
        String answer = game.getAnswer();

        String result = game.makeStep(answer);
        assertEquals("+++++", result);
        assertTrue(game.isWon());
        assertEquals(5, game.getStepsLeft());
    }

    @Test
    void testNoMatchReturnsDashes() throws IOException {
        WordleDictionary dictionary = createDictionary(Arrays.asList("гидра", "щипцы"));
        WordleGame game = new WordleGame(dictionary);

        String result = game.makeStep("гидра");
        assertEquals("-+---", result);
        assertFalse(game.isWon());
    }

    @Test
    void testGameEndsAfterSixAttempts() throws IOException {
        WordleDictionary dictionary = createDictionary(Arrays.asList("слово", "книга", "трава"));
        WordleGame game = new WordleGame(dictionary);


        for (int i = 0; i < 6; i++) {
            game.makeStep("книга");
        }

        assertTrue(game.isLost());
        assertFalse(game.isWon());
        assertEquals(0, game.getStepsLeft());
    }

    @Test
    void testHistoryRecordsAllSteps() throws IOException {
        WordleDictionary dictionary = createDictionary(Arrays.asList("слово", "книга", "трава"));
        WordleGame game = new WordleGame(dictionary);

        game.makeStep("книга");
        game.makeStep("трава");

        assertEquals(2, game.getHistory().size());
        assertTrue(game.getHistory().containsKey("книга"));
        assertTrue(game.getHistory().containsKey("трава"));
    }

    @Test
    void testHintReturnsValidWordFromDictionary() throws IOException {
        WordleDictionary dictionary = createDictionary(Arrays.asList("слово", "книга", "трава"));
        WordleGame game = new WordleGame(dictionary);
        String hint = game.getHint();

        assertNotNull(hint);
        if (!hint.startsWith("Подсказок нет")) {
            assertEquals(5, hint.length());
            assertTrue(dictionary.contains(hint), "Подсказка должна быть валидным словом из словаря");
        }
    }

    @Test
    void testMaskContainsOnlyValidSymbols() throws IOException {
        WordleDictionary dictionary = createDictionary(Arrays.asList("слово", "книга", "трава"));
        WordleGame game = new WordleGame(dictionary);
        String result = game.makeStep("книга");

        assertTrue(result.matches("[\\+\\^\\-]{5}"));
    }

}