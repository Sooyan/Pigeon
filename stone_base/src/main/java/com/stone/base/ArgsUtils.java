package com.stone.base;

/**
 * Created by Joseph.Yan.
 */
public final class ArgsUtils {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new NullPointerException(message == null ? "" : message);
        }
    }

    private ArgsUtils() {
        throw new RuntimeException(ArgsUtils.class.getName() + " can`t be implements an instance");
    }
}
