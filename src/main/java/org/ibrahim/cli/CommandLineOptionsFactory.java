package org.ibrahim.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

/**
 * Factory for creating command-line options for the cookie-cutter CLI application.
 * Provides methods to create the full set of options for normal operation and a minimal set for help detection.
 */
public class CommandLineOptionsFactory {
    /**
     * Creates the full set of command-line options for the application.
     * Includes file, date, and help options.
     *
     * @return the configured Options object
     */
    public static Options createOptions() {
        Options options = new Options();
        options.addOption(
            Option.builder("f")
                .longOpt("file")
                .desc("Path to the file to be processed")
                .hasArg()
                .argName("FILE")
                .required()
                .build());
        options.addOption(
            Option.builder("d")
                .longOpt("date")
                .desc("Date filter to find the most active cookies")
                .hasArg()
                .argName("DATE")
                .required()
                .build());
        options.addOption("?", "help", false, "Display help information");
        return options;
    }

    /**
     * Creates a minimal set of options for detecting the help flag.
     *
     * @return the Options object containing only the help option
     */
    public static Options createHelpOptions() {
        Options helpOptions = new Options();
        helpOptions.addOption("?", "help", false, "Display help information");
        return helpOptions;
    }
}
