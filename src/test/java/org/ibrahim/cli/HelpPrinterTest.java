package org.ibrahim.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


class HelpPrinterTest {
    private final HelpFormatter helpFormatter = mock(HelpFormatter.class);
    private final HelpPrinter target = new HelpPrinterImpl(helpFormatter);

    @Test
    void printHelpDoesPrint() {
        Options options = CommandLineOptionsFactory.createOptions();

        target.printHelp(options);

        verify(helpFormatter, times(1)).printHelp("cookie-cutter -d DATE -f FILENAME", options);
    }
}


