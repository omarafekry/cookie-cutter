package org.ibrahim.io;

import org.ibrahim.exception.CookieParseException;
import org.ibrahim.model.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Parses cookies from a CSV file and provides various methods for processing them.
 * <p>
 * Supports streaming, chunked, and parallel processing. Handles file errors and parsing errors
 * with custom exceptions. Uses SLF4J for logging.</p>
 * <b>Usage Example:</b>
 * <pre><code>
 * CookieParser parser = new CookieParser();
 * List&lt;Cookie&gt; cookies = parser.parseAll("cookie_log.csv");
 * </code></pre>
 */
public class CookieParser {
  private static final int CHUNK_SIZE = 3; // lines per chunk
  private static final Logger logger = LoggerFactory.getLogger(CookieParser.class);

  /**
   * Parses all cookies from the specified CSV file.
   * @param filename the path to the CSV file
   * @return a list of all parsed cookies
   * @throws CookieParseException if the file cannot be read or parsed
   */
  public List<Cookie> parseAll(String filename) {
    logger.info("Parsing file: {}", filename);
    List<Cookie> cookies = new ArrayList<>();
    try {
      FileUtils.processLines(filename, true, line -> cookies.add(parseLine(line)));
    } catch (IOException e) {
      logger.error("Error reading file: {}", filename, e);
      throw new CookieParseException("Error reading file: " + filename, e);
    }
    logger.debug("Parsed {} cookies from file: {}", cookies.size(), filename);
    return cookies;
  }

  /**
   * Parses the file in parallel, processing cookies in chunks.
   * @param filename the path to the CSV file
   * @param chunkProcessor a consumer to process each chunk of cookies
   * @throws CookieParseException if the file cannot be read or parsed
   */
  public void parseParallel(String filename, Consumer<List<Cookie>> chunkProcessor) {
    logger.info("Parsing file in parallel: {}", filename);
    List<String> chunk = new ArrayList<>(CHUNK_SIZE);
    try {
      FileUtils.processLines(filename, true, line -> {
        chunk.add(line);
        if (chunk.size() == CHUNK_SIZE) {
          logger.debug("Processing chunk of size {}", CHUNK_SIZE);
          chunkProcessor.accept(parseChunkParallel(chunk));
          chunk.clear();
        }
      });
      if (!chunk.isEmpty()) {
        logger.debug("Processing final chunk of size {}", chunk.size());
        chunkProcessor.accept(parseChunkParallel(chunk));
      }
    } catch (IOException e) {
      logger.error("Error reading file: {}", filename, e);
      throw new CookieParseException("Error reading file: " + filename, e);
    }
  }

  /**
   * Parses each cookie from the file and processes it immediately.
   * @param filename the path to the CSV file
   * @param processor a consumer to process each cookie
   * @throws CookieParseException if the file cannot be read or parsed
   */
  public void parseEach(String filename, Consumer<Cookie> processor) {
    logger.info("Parsing each cookie from file: {}", filename);
    try {
      FileUtils.processLines(filename, true, line -> {
        Cookie cookie = parseLine(line);
        logger.trace("Processing cookie: {}", cookie);
        processor.accept(cookie);
      });
    } catch (IOException e) {
      logger.error("Error reading file: {}", filename, e);
      throw new CookieParseException("Error reading file: " + filename, e);
    }
  }

  /**
   * Parses a chunk of lines in parallel into a list of cookies.
   * @param lines the lines to parse
   * @return a list of parsed cookies
   */
  private List<Cookie> parseChunkParallel(List<String> lines) {
    return lines.parallelStream()
        .map(this::parseLine)
        .collect(Collectors.toList());
  }

  /**
   * Parses a single line into a Cookie object.
   * @param line the CSV line
   * @return the parsed Cookie
   * @throws CookieParseException if the line is invalid or the date format is wrong
   */
  private Cookie parseLine(String line) {
    logger.trace("Parsing line: {}", line);
    String[] parts = line.split(",");
    if (parts.length != Cookie.class.getDeclaredFields().length) {
      logger.warn("Invalid CSV line: {}", line);
      throw new CookieParseException("Column and field count mismatch in line: " + line);
    }
    try {
      Cookie cookie = new Cookie(parts[0], Instant.parse(parts[1]));
      logger.trace("Parsed cookie: {}", cookie);
      return cookie;
    } catch (Exception e) {
      logger.warn("Invalid date format in line: {}", line);
      throw new CookieParseException("Invalid date format in line: " + line, e);
    }
  }
}
