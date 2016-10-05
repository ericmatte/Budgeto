package com.endless.tools;

import android.util.Log;

/**
 * Log data to the console easily
 * Log type: DEBUG, ERROR, INFO, VERBOSE, WARN, WTF
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class Logger {
    private static final String filterTag = "App.";
    private static final boolean enabled = true;

    public static int print(Class referrer, String message) {
        return print(referrer, message, null);
    }

    public static int print(Class referrer, String message, String title) {
        return print(referrer, message, title, Log.DEBUG);
    }

    public static int print(Class referrer, String message, int priority) {
        return print(referrer, message, null, Log.DEBUG);
    }

    public static int print(Class referrer, String message, String title, int priority) {
        String[] fullClassName = referrer.getName().split("\\.");
        String tag = filterTag + fullClassName[fullClassName.length-1];

        if (title != null && !title.isEmpty()) {
            tag += " <" + title + ">";
        }

        if (message == null) {
            message = "<empty_string>";
        }

        if (enabled) {
            return Log.println(priority, tag, message);
        } else {
            return -1;
        }
    }
}
