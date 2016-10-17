package com.endless.tools;

/**
 * Created by Eric on 2016-09-14.
 */
public class Sanitizer {

    public static String sanitize(String dirty) {
        // TODO: Sanitize.
        String clean = dirty; // .replaceAll("\\W", "");
        return clean;
    }

    public static String clean(String value) {
        if (value == null)
            return "";

        if (value.contains("\n"))
            value = value.substring(0, value.indexOf('\n'));
        if (value.contains("&"))
            value = value.substring(0, value.indexOf('&'));

        value = sanitize(value);
        value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
        value = value.trim();

        return value;
    }

    public enum StringType {
        lettersAndNumbersOnly, lettersOnly, numbersOnly, anyChars
    }

    public static String validateString(String string, int minLength, int maxLength, StringType stringType) {
        if (string == null || string.equals("")) return "empty";
        if (string.length() < minLength) return "too short";
        if (string.length() > maxLength) return "too long";

        boolean isCorrect = true;
        switch (stringType) {
            case lettersAndNumbersOnly:
                if (!string.matches("[a-zA-Z0-9]+")) isCorrect = false;
                break;
            case lettersOnly:
                if (!string.matches("[a-zA-Z]+")) isCorrect = false;
                break;
            case numbersOnly:
                if (!string.matches("[0-9]+")) isCorrect = false;
                break;
            case anyChars:
                if (!string.matches(".*[a-zA-Z0-9]+.*")) isCorrect = false;
        }

        return isCorrect ? "validated" : "error";
    }
}
