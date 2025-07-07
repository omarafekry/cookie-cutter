package org.ibrahim.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import java.time.Instant;

class CookieTest {
    @Test
    void testEquals() {
        assertEquals(
            new Cookie("abc", Instant.parse("2025-07-07T10:00:00Z")),
            new Cookie("abc", Instant.parse("2025-07-07T23:59:59Z"))
        );
      assertNotEquals(
          new Cookie("abc", Instant.parse("2025-07-07T10:00:00Z")),
          new Cookie("def", Instant.parse("2025-07-07T10:00:00Z"))
      );
      assertNotEquals(
          new Cookie("abc", Instant.parse("2025-07-07T10:00:00Z")),
          new Cookie("abc", Instant.parse("2025-07-08T00:00:00Z"))
      );
      assertNotEquals(
          new Cookie("abc", Instant.parse("2025-07-07T10:00:00Z")),
          null
      );
      assertNotEquals(
          new Cookie("abc", Instant.parse("2025-07-07T10:00:00Z")),
          "not a cookie"
      );
    }

    @Test
    void testHashCode() {
      assertEquals(
          new Cookie("abc", Instant.parse("2025-07-07T10:00:00Z")).hashCode(),
          new Cookie("abc", Instant.parse("2025-07-07T23:59:59Z")).hashCode()
      );
      assertNotEquals(
          new Cookie("abc", Instant.parse("2025-07-07T10:00:00Z")).hashCode(),
          new Cookie("def", Instant.parse("2025-07-07T10:00:00Z")).hashCode()
      );
      assertNotEquals(
          new Cookie("abc", Instant.parse("2025-07-07T10:00:00Z")).hashCode(),
          new Cookie("abc", Instant.parse("2025-07-08T00:00:00Z")).hashCode()
      );
    }
}