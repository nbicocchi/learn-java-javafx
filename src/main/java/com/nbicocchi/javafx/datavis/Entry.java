package com.nbicocchi.javafx.datavis;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Entry {
    private final String word;
    private final int frequency;

    public Entry(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }
}
