package org.ibrahim.io;

import org.ibrahim.model.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Prints a list of cookies and their details to the console.
 * <p>
 * This implementation uses SLF4J for logging and supports indexed output.</p>
 * <b>Usage Example:</b>
 * <pre><code>
 * CookiePrinter printer = new CookiePrinterImpl();
 * printer.print(cookies);
 * </code></pre>
 */
public class CookiePrinterImpl implements CookiePrinter {
    private static final Logger logger = LoggerFactory.getLogger(CookiePrinterImpl.class);

    /**
     * Prints a list of cookies to the console, with an index for each.
     * @param cookies the list of cookies to print
     */
    public void print(List<Cookie> cookies) {
        logger.info("Printing {} cookies", cookies.size());
        if (cookies.isEmpty()) {
            logger.info("No cookies found for the specified date.");
            System.out.println("No cookies found for the specified date.");
        } else {
            System.out.println("Most active cookies:");
            for (int i = 0; i < cookies.size(); i++) {
                print(cookies.get(i), i);
            }
        }
    }

    /**
     * Prints a single cookie with its index.
     * @param cookie the cookie to print
     * @param index the index of the cookie in the list
     */
    public void print(Cookie cookie, int index) {
        logger.debug("Printing cookie at index {}: {}", index, cookie);
        System.out.printf("%d. %s\n", index + 1, cookie.getValue());
    }
}
