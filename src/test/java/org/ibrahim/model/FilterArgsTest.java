package org.ibrahim.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilterArgsTest {

  @Test
  void setDate_validDate_setsDate() {
    FilterArgs args = new FilterArgs();
    args.setDate("2025-07-07");
    assertEquals(LocalDate.of(2025, 7, 7), args.getDate());
  }

  @Test
  void setDate_invalidDate_throwsException() {
    FilterArgs args = new FilterArgs();
    Exception ex = assertThrows(IllegalArgumentException.class, () -> args.setDate("not-a-date"));
    assertEquals("Invalid date format. Expected format: yyyy-MM-dd", ex.getMessage());
  }
}