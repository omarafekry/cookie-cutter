package org.ibrahim;

import org.ibrahim.business.MostActiveCookieFinder;
import org.ibrahim.cli.CommandParser;
import org.ibrahim.cli.HelpPrinter;
import org.ibrahim.cli.HelpPrinterImpl;
import org.ibrahim.exception.ExceptionHandler;
import org.ibrahim.io.CookieParser;
import org.ibrahim.io.CookiePrinter;
import org.ibrahim.io.CookiePrinterImpl;
import org.ibrahim.service.CookieService;

public class Main {
    public static void main(String[] args) {
        try {
            HelpPrinter helpPrinter = new HelpPrinterImpl();
            CommandParser commandParser = new CommandParser(helpPrinter);
            CookieParser cookieParser = new CookieParser();
            MostActiveCookieFinder mostActiveCookieFinder = new MostActiveCookieFinder(cookieParser);
            CookiePrinter printer = new CookiePrinterImpl();
            CookieService cookieService = new CookieService(commandParser, mostActiveCookieFinder, printer);
            cookieService.run(args);
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }
    }
}
