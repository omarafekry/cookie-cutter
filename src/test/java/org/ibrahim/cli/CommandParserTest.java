package org.ibrahim.cli;

import org.ibrahim.exception.CookieArgumentException;
import org.ibrahim.model.FilterArgs;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommandParserTest {
    HelpPrinter helpPrinter = mock(HelpPrinter.class);
    CommandParser target = new CommandParser(helpPrinter);

    @Test
    void testParseArgsValid() {
        String[] args = {"-f", "cookie_log.csv", "-d", "2025-07-06"};

        Optional<FilterArgs> result = target.parseArgs(args);

        assertTrue(result.isPresent());
        FilterArgs filterArgs = result.get();
        assertEquals("cookie_log.csv", filterArgs.getFilename());
        assertEquals("2025-07-06", filterArgs.getDate().toString());
        verify(helpPrinter, never()).printHelp(any());
    }

    @Test
    void testParseArgsHelp() {
        String[] args = {"--help"};

        Optional<FilterArgs> result = target.parseArgs(args);

        assertTrue(result.isEmpty());
        verify(helpPrinter, times(1)).printHelp(any());
    }

    @Test
    void testParseArgsInvalid() {
        String[] args = {"-f"};

        assertThrows(CookieArgumentException.class, () -> target.parseArgs(args));
        verify(helpPrinter, atLeastOnce()).printHelp(any());
    }
}

