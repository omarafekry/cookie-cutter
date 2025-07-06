package org.ibrahim.exception;

public class CookieParseException extends CookieAppException {
    public CookieParseException(String message) {
        super(message);
    }
    public CookieParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

