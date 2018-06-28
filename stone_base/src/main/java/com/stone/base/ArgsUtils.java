package com.stone.base;

/**
 * Created by Joseph.Yan.
 */
public final class ArgsUtils {

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new NullPointerException(message == null ? "" : message);
        }
    }

    private ArgsUtils() {
        throw new RuntimeException(ArgsUtils.class.getName() + " can`t be implements an instance");
    }
}
