package com.stone.base.exception;

/**
 * Created by Joseph.Yan.
 */
public class RepeatException extends RuntimeException {

    public RepeatException() {
    }

    public RepeatException(String message) {
        super(message);
    }

    public RepeatException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatException(Throwable cause) {
        super(cause);
    }
}
