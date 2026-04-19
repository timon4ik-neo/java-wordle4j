package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleDictionary {
    private final List<String> words = new ArrayList<>();
    private final Random random = new Random();

    public WordleDictionary(String filename) throws IOException {
        loadDictionary(filename);
    }

    private void loadDictionary(String filename) throws IOException {
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader(filename);
            reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                String normalized = line.trim().toLowerCase().replace('ё', 'е');
                if (normalized.length() == 5 && normalized.matches("[а-я]+")) {
                    words.add(normalized);
                }
            }
        } finally {
            if (reader != null) reader.close();
            if (fileReader != null) fileReader.close();
        }
    }

    public String getRandomWord() {
        if (words.isEmpty()) return null;
        return words.get(random.nextInt(words.size()));
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public List<String> getWords() {
        return words;
    }

    public int size() {
        return words.size();
    }
}