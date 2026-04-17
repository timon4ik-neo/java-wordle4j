package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleDictionary {

    private ArrayList<String> words = new ArrayList<>();
    private final Random random = new Random();

    public void loadDictionary(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String word = normalize(line);
                if (word.length() == 5) {
                    words.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Произошла ошибка,файл не найден");
        } catch (IOException e) {
            throw new RuntimeException("Во-время обработки слов произошла непредвиденная ошибка");
        }
        System.out.println("Успешная обработка словаря");
    }
    public String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim().toLowerCase().replace('ё',  'е');
    }
    public String getRandomWord() {
        if (words.isEmpty()) {
            return words.get(random.nextInt(0, words.size()));
        }
        throw new RuntimeException("Данный файл не найден");
    }

    public ArrayList<String> getWords() {
        return words;
    }

}
