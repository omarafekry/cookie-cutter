package org.ibrahim.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineOptionsFactoryTest {
    @Test
    void testCreateOptionsContainsAllRequiredOptions() {
        Options options = CommandLineOptionsFactory.createOptions();
        Option fileOption = options.getOption("f");
        Option dateOption = options.getOption("d");
        Option helpOption = options.getOption("?");
        assertNotNull(fileOption, "File option should be present");
        assertNotNull(dateOption, "Date option should be present");
        assertNotNull(helpOption, "Help option should be present");
        assertTrue(fileOption.isRequired(), "File option should be required");
        assertTrue(dateOption.isRequired(), "Date option should be required");
        assertFalse(helpOption.isRequired(), "Help option should not be required");
        assertTrue(fileOption.hasArg(), "File option should have an argument");
        assertTrue(dateOption.hasArg(), "Date option should have an argument");
        assertEquals("file", fileOption.getLongOpt());
        assertEquals("date", dateOption.getLongOpt());
        assertEquals("help", helpOption.getLongOpt());
    }

    @Test
    void testCreateHelpOptionsContainsOnlyHelp() {
        Options helpOptions = CommandLineOptionsFactory.createHelpOptions();
        assertEquals(1, helpOptions.getOptions().size(), "Help options should contain only one option");
        Option helpOption = helpOptions.getOption("?");
        assertNotNull(helpOption, "Help option should be present");
        assertEquals("help", helpOption.getLongOpt());
        assertFalse(helpOption.isRequired(), "Help option should not be required");
    }
}

