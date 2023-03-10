package com.nbicocchi.javafx.datavis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class WordExtractor {

    public static List<Map.Entry<String, Long>> extract(String filename) {
        try {
            Map<String, Long> frequencies =
                    Files.readAllLines(Path.of(filename)).stream()
                            .flatMap(line -> Arrays.stream(line.split(" +")))
                            .map(word -> word.toLowerCase())
                            .filter(word -> isWord(word))
                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            return new ArrayList<>(frequencies.entrySet());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private static boolean isWord(String word) {
        if (word.length() < 4)
            return false;
        if (word.isEmpty())
            return false;
        for (char c : word.toCharArray()) {
            if (!Character.isLetter(c))
                return false;
        }
        return true;
    }
}
