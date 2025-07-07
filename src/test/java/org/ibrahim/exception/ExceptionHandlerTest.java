package org.ibrahim.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExceptionHandlerTest {
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void testHandleAllExceptions() {
        ExceptionHandler.handle(new IOException("Test IO error"));
        ExceptionHandler.handle(new CookieParseException("Test parse error"));
        ExceptionHandler.handle(new CookieArgumentException("Test argument error", null));
        ExceptionHandler.handle(new CookieAppException("Test app error"));
        ExceptionHandler.handle(new Exception("Test unknown error"));
        String output = errContent.toString();
        assertTrue(output.contains("File error: Test IO error"));
        assertTrue(output.contains("Parse error: Test parse error"));
        assertTrue(output.contains("Argument error: Test argument error"));
        assertTrue(output.contains("Application error: Test app error"));
        assertTrue(output.contains("Unexpected error: Test unknown error"));
    }
}