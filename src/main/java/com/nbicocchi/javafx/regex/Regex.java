package com.nbicocchi.javafx.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static void main(String[] args) {
        matchHowTo();
        groupsHowTo();
        example();
    }

    public static void matchHowTo() {
        boolean b;
        // 1st way
        Pattern p = Pattern.compile(".s");//. represents single character
        Matcher m = p.matcher("as");
        b = m.matches();
        System.out.println(b);
        // 2nd way
        b = Pattern.compile(".s").matcher("as").matches();
        System.out.println(b);
        // 3rd way
        b = Pattern.matches(".s", "as");
        System.out.println(b);
        // 4th way
        b = "as".matches(".s");
        System.out.println(b);
    }

    public static void groupsHowTo() {
        // String to be scanned to find the pattern.
        String line = "Ordered 3000 items having code ABCD.";
        String pattern = "(\\d+) items (.*) code (\\S+).";
        // Create a Pattern object
        Pattern p = Pattern.compile(pattern);
        // Now create matcher object.
        Matcher m = p.matcher(line);
        if (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                System.out.printf("group=%d, value=%s\n", i, m.group(i));
            }
        } else {
            System.out.println("[no match]");
        }
    }

    public static void example() {
        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(Regex.class.getResourceAsStream("regex_examples.csv"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isBlank() && !line.startsWith("#")) {
                    lines.add(line);
                }
            }
        }
        for (String line : lines) {
            String[] v = line.split(", ");
            String pattern = v[0].substring(1, v[0].length() - 1);
            String test = v[1].substring(1, v[1].length() - 1);
            boolean result = Pattern.matches(pattern, test);
            System.out.printf("[%b] regex=%s test=%s\n", result, pattern, test);
        }
    }
}
