package org.ibrahim.service;

import org.ibrahim.business.MostActiveCookieFinder;
import org.ibrahim.cli.CommandParser;
import org.ibrahim.io.CookiePrinter;
import org.ibrahim.model.Cookie;
import org.ibrahim.model.FilterArgs;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class CookieServiceTest {
  CommandParser commandParser = mock(CommandParser.class);
  MostActiveCookieFinder finder = mock(MostActiveCookieFinder.class);
  CookiePrinter printer = mock(CookiePrinter.class);
  CookieService target = new CookieService(commandParser, finder, printer);

  @Test
  void testRun_withFilterArgs_Runs() {

    String[] args = {"-f", "file.csv", "-d", "2025-07-07"};
    FilterArgs filterArgs = new FilterArgs();
    filterArgs.setDate("2025-07-07");
    filterArgs.setFilename("file.csv");
    when(commandParser.parseArgs(args)).thenReturn(Optional.of(filterArgs));
    List<Cookie> cookies = List.of(mock(Cookie.class));
    when(finder.findMostActiveCookies("file.csv", LocalDate.parse("2025-07-07"))).thenReturn(cookies);

    target.run(args);

    verify(commandParser).parseArgs(args);
    verify(finder).findMostActiveCookies("file.csv", LocalDate.parse("2025-07-07"));
    verify(printer).print(cookies);
    verifyNoMoreInteractions(commandParser, finder, printer);
  }

  @Test
  void testRun_noFilterArgs_noFurtherCalls() {
    String[] args = {"-f", "file.csv", "-d", "2025-07-07"};
    when(commandParser.parseArgs(args)).thenReturn(Optional.empty());

    target.run(args);

    verify(commandParser).parseArgs(args);
    verifyNoMoreInteractions(commandParser, finder, printer);
  }
}