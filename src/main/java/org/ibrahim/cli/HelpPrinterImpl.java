package org.ibrahim.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Prints help information for the CLI application using Apache Commons CLI.
 * <p>
 * This implementation uses SLF4J for logging and delegates to HelpFormatter for formatting.</p>
 * <b>Usage Example:</b>
 * <pre><code>
 * HelpPrinter helpPrinter = new HelpPrinterImpl();
 * helpPrinter.printHelp(options);
 * </code></pre>
 */
public class HelpPrinterImpl implements HelpPrinter {
    String COMMAND_LINE_SYNTAX = "cookie-cutter -d DATE -f FILENAME";
    private final HelpFormatter helpFormatter;
    private  final Logger logger = LoggerFactory.getLogger(HelpPrinterImpl.class);

    /**
     * Constructs a HelpPrinterImpl with a default HelpFormatter and logger.
     */
    public HelpPrinterImpl(HelpFormatter helpFormatter) {
        this.helpFormatter = helpFormatter;
    }

    /**
     * Prints the help message for the application with the given options.
     * @param options the CLI options to display in the help message
     */
    public void printHelp(Options options) {
        logger.info("Parsing help options and printing help message");
        helpFormatter.printHelp(COMMAND_LINE_SYNTAX, options);
    }
}
