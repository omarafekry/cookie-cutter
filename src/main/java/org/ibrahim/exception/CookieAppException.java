package org.ibrahim.exception;

public class CookieAppException extends RuntimeException {
    public CookieAppException(String message) {
        super(message);
    }
    public CookieAppException(String message, Throwable cause) {
        super(message, cause);
    }
}

