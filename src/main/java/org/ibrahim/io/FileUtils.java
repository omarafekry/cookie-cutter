package org.ibrahim.io;

import java.io.*;

/**
 * Utility class for file operations and line-by-line processing.
 * <p>
 * Provides methods to open files safely and process each line (skipping the header) using a functional interface.
 */
public class FileUtils {
    /**
     * Functional interface for processing a line from a file.
     */
    @FunctionalInterface
    public interface LineProcessor {
        void process(String line) throws IOException;
    }

    /**
     * Opens a file for reading, throwing an exception if it does not exist.
     * @param filename the path to the file
     * @return a BufferedReader for the file
     * @throws IOException if the file does not exist or cannot be opened
     */
    public static BufferedReader openFileOrThrow(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filename);
        }
        return new BufferedReader(new FileReader(file));
    }

    /**
     * Processes each line of a file using the given processor.
     * @param filename the path to the file
     * @param skipHeader if true, the first line (header) will be skipped
     * @param processor the processor to apply to each line
     * @throws IOException if the file cannot be read
     */
    public static void processLines(String filename, boolean skipHeader, LineProcessor processor) throws IOException {
        try (BufferedReader reader = openFileOrThrow(filename)) {
            if (skipHeader) {
                reader.readLine(); // Skip the header line
            }
            String line;
            while ((line = reader.readLine()) != null) {
                processor.process(line);
            }
        }
    }
}
