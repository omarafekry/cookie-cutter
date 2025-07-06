package org.ibrahim.service;

import org.ibrahim.business.MostActiveCookieFinder;
import org.ibrahim.cli.CommandParser;
import org.ibrahim.io.CookiePrinter;
import org.ibrahim.model.Cookie;
import org.ibrahim.model.FilterArgs;
import java.util.List;
import java.util.Optional;

/**
 * CookieService encapsulates the main workflow of the application:
 * argument parsing, business logic, and output.
 */
public class CookieService {
    private final CommandParser commandParser;
    private final MostActiveCookieFinder mostActiveCookieFinder;
    private final CookiePrinter printer;

    public CookieService(CommandParser commandParser, MostActiveCookieFinder mostActiveCookieFinder, CookiePrinter printer) {
        this.commandParser = commandParser;
        this.mostActiveCookieFinder = mostActiveCookieFinder;
        this.printer = printer;
    }

    /**
     * Runs the main workflow: parses args, finds most active cookies, prints results.
     * @param args command-line arguments
     */
    public void run(String[] args) {
        Optional<FilterArgs> optionalFilterArgs = commandParser.parseArgs(args);
        if (optionalFilterArgs.isEmpty()) {
            return;
        }
        FilterArgs filterArgs = optionalFilterArgs.get();
        List<Cookie> mostActiveCookies = mostActiveCookieFinder.findMostActiveCookies(
            filterArgs.getFilename(),
            filterArgs.getDate()
        );
        printer.print(mostActiveCookies);
    }
}

