package org.ibrahim.exception;

import java.io.IOException;

/**
 * Centralized exception handler for the cookie-cutter application.
 * <p>
 * Handles and prints user-friendly error messages for known exception types.
 */
public class ExceptionHandler {
    /**
     * Handles the given exception by printing a user-friendly error message to the standard error output stream.
     * @param e the exception to handle
     */
    public static void handle(Exception e) {
      switch (e) {
        case IOException _ -> System.err.println("File error: " + e.getMessage());
        case CookieParseException _ -> System.err.println("Parse error: " + e.getMessage());
        case CookieArgumentException _ -> System.err.println("Argument error: " + e.getMessage());
        case CookieAppException _ -> System.err.println("Application error: " + e.getMessage());
        default -> System.err.println("Unexpected error: " + e.getMessage());
      }
    }
}
