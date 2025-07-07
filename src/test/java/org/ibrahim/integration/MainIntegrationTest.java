package org.ibrahim.integration;

import org.ibrahim.Main;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainIntegrationTest {
    @Test
    void testMain_withValidArgs_printsMostActiveCookies() {
        String[] args = {"-f", "src/test/resources/test_cookies.csv", "-d", "2025-07-07"};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        try {
            Main.main(args);
        } finally {
            System.setOut(originalOut);
        }
        String output = out.toString();
        assertTrue(output.contains("abc"));
        assertTrue(output.contains("def"));
    }

    @Test
    void testMain_withNoCookiesOnDate_printsNothing() {
        String[] args = {"-f", "src/test/resources/test_cookies.csv", "-d", "2025-07-09"};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        try {
            Main.main(args);
        } finally {
            System.setOut(originalOut);
        }
        String output = out.toString();
        assertTrue(output.contains("No cookies found for the specified date."));
    }

    @Test
    void testMain_withOneCookieOnDate_printsSingleCookie() {
        String[] args = {"-f", "src/test/resources/test_cookies.csv", "-d", "2025-07-08"};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        try {
            Main.main(args);
        } finally {
            System.setOut(originalOut);
        }
        String output = out.toString();
        assertTrue(output.contains("ghi"));
    }

    @Test
    void testMain_withInvalidFile_printsError() {
        String[] args = {"-f", "src/test/resources/nonexistent.csv", "-d", "2025-07-07"};
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(err));
        try {
            Main.main(args);
        } finally {
            System.setErr(originalErr);
        }
        String errorOutput = err.toString();
        assertTrue(errorOutput.contains("Parse error: Error reading file:"));
    }

    @Test
    void testMain_withInvalidDateFormat_printsError() {
        String[] args = {"-f", "src/test/resources/test_cookies.csv", "-d", "not-a-date"};
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(err));
        try {
            Main.main(args);
        } finally {
            System.setErr(originalErr);
        }
        String errorOutput = err.toString();
        assertTrue(errorOutput.toLowerCase().contains("invalid date format"));
    }
}
