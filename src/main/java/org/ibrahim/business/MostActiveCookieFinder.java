package org.ibrahim.business;

import org.ibrahim.io.CookieParser;
import org.ibrahim.model.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Business logic for finding the most active cookies for a given date from a CSV log file.
 * <p>
 * This class uses a streaming approach to efficiently count and determine the most active cookies
 * (by value and calendar date) for a specified date. It relies on a {@link CookieParser} to read
 * cookies from a file and supports logging via SLF4J.</p>
 * <b>Usage Example:</b>
 * <code>
 * MostActiveCookieFinder finder = new MostActiveCookieFinder(new CookieParser());
 * List&lt;Cookie&gt; mostActive = finder.findMostActiveCookies("cookie_log.csv", LocalDate.parse("2025-07-06"));
 * </code>
 */
public class MostActiveCookieFinder {
  private final CookieParser cookieParser;
  private static final Logger logger = LoggerFactory.getLogger(MostActiveCookieFinder.class);

  /**
   * Constructs a MostActiveCookieFinder with the given CookieParser.
   * @param cookieParser the parser to use for reading cookies from a file
   */
  public MostActiveCookieFinder(CookieParser cookieParser) {
    this.cookieParser = cookieParser;
  }

  /**
   * Finds the most active cookies for a given date from the specified CSV file.
   * @param filename the path to the CSV file
   * @param date the date to filter cookies by
   * @return a list of the most active cookies for the given date
   */
  public List<Cookie> findMostActiveCookies(String filename, LocalDate date) {
    logger.info("Finding most active cookies for file: {} and date: {}", filename, date);
    Map<Cookie, Integer> cookieCount = countCookiesOnDate(filename, date);
    int max = findMaxCount(cookieCount);
    logger.debug("Max cookie count for date {}: {}", date, max);
    return findCookiesWithCount(cookieCount, max);
  }

  /**
   * Parses and counts the occurrences of each cookie for the specified date.
   * Only cookies matching the given date (UTC, yyyy-MM-dd) are counted.
   * @param filename the path to the CSV file
   * @param date the date to filter cookies by
   * @return a map of Cookie to its count for the given date
   */
  private Map<Cookie, Integer> countCookiesOnDate(String filename, LocalDate date) {
    logger.debug("Counting cookies for file: {} and date: {}", filename, date);
    Map<Cookie, Integer> cookieCount = new LinkedHashMap<>();
    Consumer<Cookie> cookieProcessor = cookie -> {
      LocalDate cookieDate = cookie.getTimestamp().atZone(ZoneOffset.UTC).toLocalDate();
      if (cookieDate.equals(date)) {
        cookieCount.merge(cookie, 1, Integer::sum);
        logger.trace("Cookie counted: {} on {}", cookie.getValue(), cookieDate);
      }
    };
    cookieParser.parseEach(filename, cookieProcessor);
//    cookieParser.parseAll(filename).forEach(cookieProcessor);
//    cookieParser.parseParallel(filename, chunk -> chunk.forEach(cookieProcessor));
    logger.debug("Total unique cookies for date {}: {}", date, cookieCount.size());
    return cookieCount;
  }

  /**
   * Finds the maximum count value in the cookie count map.
   * @param cookieCount the map of Cookie to count
   * @return the maximum count value, or 0 if the map is empty
   */
  private int findMaxCount(Map<Cookie, Integer> cookieCount) {
    return cookieCount.values().stream().max(Integer::compareTo).orElse(0);
  }

  /**
   * Returns a list of cookies that have the specified count.
   * @param cookieCount the map of Cookie to count
   * @param max the count to filter by
   * @return a list of cookies with the given count
   */
  private List<Cookie> findCookiesWithCount(Map<Cookie, Integer> cookieCount, int max) {
    return cookieCount.entrySet().stream()
        .filter(entry -> entry.getValue() == max)
        .map(Map.Entry::getKey).toList();
  }
}
