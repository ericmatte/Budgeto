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
}
