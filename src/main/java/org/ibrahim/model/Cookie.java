package org.ibrahim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;
import java.util.Objects;

/**
 * Represents a cookie entry parsed from the CSV log file.
 * Equality and hash code are based on the cookie value and the calendar date (UTC) of the timestamp,
 * so cookies with the same value and date (regardless of time) are considered equal.
 */
@AllArgsConstructor
@Data
public class Cookie {
  String value;
  Instant timestamp;

  /**
   * Checks equality based on value and the date part (UTC) of the timestamp.
   * @param o the object to compare
   * @return true if the value and date are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Cookie cookie = (Cookie) o;
    // Compare value and only the date part (UTC) of the Instant
    return Objects.equals(value, cookie.value)
        && timestamp.atZone(java.time.ZoneOffset.UTC).toLocalDate()
            .equals(cookie.timestamp.atZone(java.time.ZoneOffset.UTC).toLocalDate());
  }

  /**
   * Computes hash code based on value and the date part (UTC) of the timestamp.
   * @return the hash code
   */
  @Override
  public int hashCode() {
    // Use value and only the date part (UTC) of the Instant
    return Objects.hash(value, timestamp.atZone(java.time.ZoneOffset.UTC).toLocalDate());
  }
}
