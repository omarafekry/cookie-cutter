package org.ibrahim.io;

import org.ibrahim.model.Cookie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class CookiePrinterTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final CookiePrinter target = new CookiePrinterImpl();

    @BeforeAll
    static void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void tearDown() {
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    void print_emptyList_printsNoCookiesMessage() {
        target.print(Collections.emptyList());
        String output = outContent.toString().trim();
        assert output.equals("No cookies found for the specified date.");
    }

    @Test
    void print_nonEmptyList_printsCookiesWithIndexes() {
        List<Cookie> cookies = Arrays.asList(
            new Cookie("cookie1", Instant.parse("2018-04-24T10:15:30Z")),
            new Cookie("cookie2", Instant.parse("2018-04-25T10:15:30Z"))
        );
        target.print(cookies);
        String output = outContent.toString().trim();
        assert(output).contains("Most active cookies:");
        assert(output).contains("1. cookie1");
        assert(output).contains("2. cookie2");
    }

    @Test
    void print_singleCookie_printsWithIndex() {
        Cookie cookie = new Cookie("cookieX", Instant.parse("2018-04-24T10:15:30Z"));

        target.print(cookie, 0);

        String output = outContent.toString().trim();
        assert(output).equals("1. cookieX");
    }
}

