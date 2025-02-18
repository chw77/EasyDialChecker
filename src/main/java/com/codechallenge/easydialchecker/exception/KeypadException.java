package com.codechallenge.easydialchecker.exception;

public class KeypadException extends Exception {
    public KeypadException() {
        super();
    }

    public KeypadException(String message) {
        super(message);
    }

    public KeypadException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeypadException(Throwable cause) {
        super(cause);
    }

    protected KeypadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
