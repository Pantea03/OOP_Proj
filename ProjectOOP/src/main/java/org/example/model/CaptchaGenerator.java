package org.example.model;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CaptchaGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

//    public static String generateCaptcha(int length) {
//        StringBuilder captcha = new StringBuilder(length);
//        for (int i = 0; i < length; i++) {
//            captcha.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
//        }
//        return captcha.toString();
//    }

    public static Map<Character, String> generateAsciiArtMap(String captcha) {
        Map<Character, String> asciiArtMap = new HashMap<>();
        for (char c : captcha.toCharArray()) {
            asciiArtMap.put(c, getAsciiArt(c));
        }
        return asciiArtMap;
    }

    private static String getAsciiArt(char c) {
        // Example ASCII art generator (you can replace this with your actual logic)
        return switch (c) {
            case 'A' -> "  A  \n A A \nAAAAA\nA   A\nA   A";
            case 'B' -> "BBBB \nB   B\nBBBB \nB   B\nBBBB ";
            case 'C' -> " CCC \nC   C\nC    \nC   C\n CCC ";
            // Add cases for other characters
            default -> String.valueOf(c);
        };
    }
}

