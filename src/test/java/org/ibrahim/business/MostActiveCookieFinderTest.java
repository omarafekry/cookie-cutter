package org.ibrahim.business;

import org.ibrahim.io.CookieParser;
import org.ibrahim.model.Cookie;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class MostActiveCookieFinderTest {
    CookieParser parser = mock(CookieParser.class);
    MostActiveCookieFinder target = new MostActiveCookieFinder(parser);

    @Test
    void testFindMostActiveCookies_multipleMostActive() {

        doAnswer(invocation -> {
            Consumer<Cookie> consumer = invocation.getArgument(1);
            consumer.accept(new Cookie("cookie1", Instant.parse("2024-07-05T10:00:00Z")));
            consumer.accept(new Cookie("cookie2", Instant.parse("2024-07-05T11:00:00Z")));
            consumer.accept(new Cookie("cookie3", Instant.parse("2024-07-06T12:00:00Z")));
            return null;
        }).when(parser).parseEach(eq("file.csv"), any());

        var result = target.findMostActiveCookies("file.csv", LocalDate.parse("2024-07-05"));

        assertEquals(2, result.size());
        assertEquals("cookie1", result.get(0).getValue());
        assertEquals("cookie2", result.get(1).getValue());
    }

    @Test
    void testFindMostActiveCookies_oneMostActive() {

        doAnswer(invocation -> {
            Consumer<Cookie> consumer = invocation.getArgument(1);
            consumer.accept(new Cookie("cookie1", Instant.parse("2024-07-05T10:00:00Z")));
            consumer.accept(new Cookie("cookie1", Instant.parse("2024-07-05T11:00:00Z")));
            consumer.accept(new Cookie("cookie2", Instant.parse("2024-07-05T12:00:00Z")));
            return null;
        }).when(parser).parseEach(eq("file.csv"), any());

        var result = target.findMostActiveCookies("file.csv", LocalDate.parse("2024-07-05"));

        assertEquals(1, result.size());
        assertEquals("cookie1", result.getFirst().getValue());
    }

    @Test
    void testFindMostActiveCookies_noCookiesOnDate() {
        doNothing().when(parser).parseEach(eq("file.csv"), any());

        var result = target.findMostActiveCookies("file.csv", LocalDate.parse("2025-07-09"));

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindMostActiveCookies_oneCookieOnDate() {
        doAnswer(invocation -> {
            Consumer<Cookie> consumer = invocation.getArgument(1);
            consumer.accept(new Cookie("cookie1", Instant.parse("2025-07-08T12:00:00Z")));
            return null;
        }).when(parser).parseEach(eq("file.csv"), any());

        var result = target.findMostActiveCookies("file.csv", LocalDate.parse("2025-07-08"));

        assertEquals(1, result.size());
        assertEquals("cookie1", result.getFirst().getValue());
    }
}
