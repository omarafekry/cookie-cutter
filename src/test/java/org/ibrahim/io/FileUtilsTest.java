package org.ibrahim.io;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileUtilsTest {
    @Test
    void testOpenFileOrThrow_success() throws IOException {
        BufferedReader reader = FileUtils.openFileOrThrow("src/test/resources/test_cookies.csv");
        String header = reader.readLine();
        assertEquals("id,timestamp", header);
        reader.close();
    }

    @Test
    void testOpenFileOrThrow_fileNotFound() {
        Exception exception = assertThrows(FileNotFoundException.class, () ->
            FileUtils.openFileOrThrow("src/test/resources/nonexistent.csv"));
        assertTrue(exception.getMessage().contains("File not found"));
    }

    @Test
    void testProcessLines_skipHeader() throws IOException {
        FileUtils.LineProcessor processor = mock(FileUtils.LineProcessor.class);
        FileUtils.processLines("src/test/resources/test_cookies.csv", true, processor);
        InOrder inOrder = inOrder(processor);
        inOrder.verify(processor).process("abc,2025-07-07T10:00:00+00:00");
        inOrder.verify(processor).process("def,2025-07-07T11:00:00+00:00");
        inOrder.verify(processor).process("ghi,2025-07-08T12:00:00+00:00");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void testProcessLines_noSkipHeader() throws IOException {
        List<String> lines = new ArrayList<>();
        FileUtils.processLines("src/test/resources/test_cookies.csv", false, lines::add);
        assertEquals(4, lines.size());
        assertEquals("id,timestamp", lines.getFirst());
    }
}