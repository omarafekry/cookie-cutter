package org.ibrahim.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * Represents the parsed command-line arguments for the cookie-cutter application.
 */
@Data
public class FilterArgs {
  String filename;
  LocalDate date;

  /**
   * Sets the date field by parsing the given string. Throws an exception if the format is invalid.
   * @param date the date string in yyyy-MM-dd format
   * @throws IllegalArgumentException if the date format is invalid
   */
  public void setDate(String date) throws IllegalArgumentException {
    try {
      this.date = LocalDate.parse(date);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
    }
  }
}
