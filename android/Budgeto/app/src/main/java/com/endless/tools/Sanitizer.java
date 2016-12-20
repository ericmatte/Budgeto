package com.endless.tools;

/**
 * Class for cleaning string values
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class Sanitizer {

    public static String title(String str) {
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
            for (int i = 1; i < words.length; i++) {
                sb.append(" ");
                if (words[i].length() > 3)
                    sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
                else
                    sb.append(words[i].toLowerCase());
            }
        }
        return sb.toString();
    }

    public static String cleanString(String str) {
        if (str == null) return "";
        return title(str.replaceAll("\u00A0",""));
    }

    public static Float stringToFloat(String str) {
        str = cleanString(str);
        str = str.equals("") ? "0" : str;
        return Float.parseFloat(str.replace(',', '.').replaceAll("[^0-9.]", ""));
    }

    public static String stringDateFormat(String str) {
        str = cleanString(str);
        str = str.replace('/',':');
        return str.matches("[0-9]+-[0-9]+-[0-9]+") ? str : "";
    }

    public enum StringType {
        lettersAndNumbersOnly, lettersOnly, numbersOnly, anyChars
    }

    @Deprecated
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
