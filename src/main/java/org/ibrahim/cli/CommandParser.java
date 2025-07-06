package org.ibrahim.cli;

import org.apache.commons.cli.*;
import org.ibrahim.exception.CookieArgumentException;
import org.ibrahim.model.FilterArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * CommandParser is responsible for parsing and validating command-line arguments
 * for the cookie-cutter application. It handles the help option separately to provide
 * user-friendly help output and throws specific exceptions for invalid arguments.
 */
public class CommandParser {
    private static final Logger logger = LoggerFactory.getLogger(CommandParser.class);
    private final Options options = CommandLineOptionsFactory.createOptions();
    private final HelpPrinter helpPrinter;

    /**
     * Constructs a CommandParser with the given HelpPrinter implementation.
     * @param helpPrinter the HelpPrinter to use for displaying help messages
     */
    public CommandParser(HelpPrinter helpPrinter) {
        this.helpPrinter = helpPrinter;
    }

    /**
     * Prints the help message for the application.
     */
    private void printHelp() {
        logger.info("Printing help message");
        helpPrinter.printHelp(options);
    }

    /**
     * Checks if the help option is present in the command-line arguments.
     * @param args the command-line arguments
     * @return true if help is requested, false otherwise
     */
    private boolean isHelpRequested(String[] args) {
        Options helpOptions = CommandLineOptionsFactory.createHelpOptions();
        try {
            CommandLine helpCmd = new DefaultParser().parse(helpOptions, args, true);
            return helpOptions.getOptions().stream().anyMatch(helpOption -> helpCmd.hasOption(helpOption.getOpt()));
        } catch (ParseException ignored) {
            return false;
        }
    }

    /**
     * Parses the command-line arguments. If help is requested, prints help and returns empty.
     *
     * @param args command-line arguments
     * @return Optional FilterArgs if valid, or empty if help was requested
     * @throws CookieArgumentException if arguments are invalid
     */
    public Optional<FilterArgs> parseArgs(String[] args) {
        logger.info("Parsing command-line arguments");
        if (isHelpRequested(args)) {
            printHelp();
            return Optional.empty();
        }
        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            FilterArgs filterArgs = new FilterArgs();
            filterArgs.setFilename(cmd.getOptionValue("f"));
            filterArgs.setDate(cmd.getOptionValue("d"));
            logger.debug("Parsed arguments: filename={}, date={}", filterArgs.getFilename(), filterArgs.getDate());
            return Optional.of(filterArgs);
        } catch (ParseException | IllegalArgumentException e) {
            logger.error("Invalid command-line arguments", e);
            printHelp();
            throw new CookieArgumentException(e.getMessage(), e);
        }
    }
}
