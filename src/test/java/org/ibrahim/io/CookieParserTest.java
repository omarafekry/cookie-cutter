package org.ibrahim.io;

import org.ibrahim.model.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CookieParserTest {
  private final CookieParser target = new CookieParser();

  @Test
  void testParseEach_callsConsumerInOrder() {
    @SuppressWarnings("unchecked")
    Consumer<Cookie> consumer = mock(Consumer.class);
    try (MockedStatic<FileUtils> utilities = mockStatic(FileUtils.class)) {
      utilities.when(() -> FileUtils.processLines(anyString(), eq(true), any(FileUtils.LineProcessor.class)))
          .thenAnswer(invocation -> {
            FileUtils.LineProcessor processor = invocation.getArgument(2);
            processor.process("abc,2025-07-07T10:00:00Z");
            processor.process("def,2025-07-07T11:00:00Z");
            processor.process("ghi,2025-07-08T12:00:00Z");
            return null;
          });
      target.parseEach("file.csv", consumer);
      InOrder inOrder = inOrder(consumer);
      inOrder.verify(consumer).accept(new Cookie("abc", Instant.parse("2025-07-07T10:00:00Z")));
      inOrder.verify(consumer).accept(new Cookie("def", Instant.parse("2025-07-07T11:00:00Z")));
      inOrder.verify(consumer).accept(new Cookie("ghi", Instant.parse("2025-07-08T12:00:00Z")));
      inOrder.verifyNoMoreInteractions();
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  void testParseEach_emptyFile() {
    @SuppressWarnings("unchecked")
    Consumer<Cookie> consumer = mock(Consumer.class);
    try (MockedStatic<FileUtils> utilities = mockStatic(FileUtils.class)) {
      utilities.when(() -> FileUtils.processLines(anyString(), eq(true), any(FileUtils.LineProcessor.class)))
          .thenAnswer(_ -> null);
      target.parseEach("file.csv", consumer);
      verifyNoInteractions(consumer);
      utilities.verify(() -> FileUtils.processLines(eq("file.csv"), eq(true), any(FileUtils.LineProcessor.class)));
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  void testParseAll_returnsAllCookies() {
    try (MockedStatic<FileUtils> utilities = mockStatic(FileUtils.class)) {
      utilities.when(() -> FileUtils.processLines(anyString(), eq(true), any(FileUtils.LineProcessor.class)))
          .thenAnswer(invocation -> {
            FileUtils.LineProcessor processor = invocation.getArgument(2);
            processor.process("abc,2025-07-07T10:00:00Z");
            processor.process("def,2025-07-07T11:00:00Z");
            processor.process("ghi,2025-07-08T12:00:00Z");
            return null;
          });

      List<Cookie> result = target.parseAll("file.csv");

      assertEquals(3, result.size());
      assertEquals("abc", result.get(0).getValue());
      assertEquals("def", result.get(1).getValue());
      assertEquals("ghi", result.get(2).getValue());
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  void testParseAll_emptyFile() {
    try (MockedStatic<FileUtils> utilities = mockStatic(FileUtils.class)) {
      utilities.when(() -> FileUtils.processLines(anyString(), eq(true), any(FileUtils.LineProcessor.class)))
          .thenAnswer(_ -> null); // No lines processed
      List<Cookie> result = target.parseAll("file.csv");
      assertEquals(0, result.size());
      utilities.verify(() -> FileUtils.processLines(eq("file.csv"), eq(true), any(FileUtils.LineProcessor.class)));
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  void testParseParallel_twoChunksDiffSize() {
    @SuppressWarnings("unchecked")
    Consumer<List<Cookie>> chunkConsumer = mock(Consumer.class);
    try (MockedStatic<FileUtils> utilities = mockStatic(FileUtils.class)) {
      utilities.when(() -> FileUtils.processLines(anyString(), eq(true), any(FileUtils.LineProcessor.class)))
          .thenAnswer(invocation -> {
            FileUtils.LineProcessor processor = invocation.getArgument(2);
            processor.process("abc,2025-07-07T10:00:00Z");
            processor.process("def,2025-07-07T11:00:00Z");
            processor.process("ghi,2025-07-08T12:00:00Z");
            processor.process("xyz,2025-07-08T13:00:00Z");
            return null;
          });

      target.parseParallel("file.csv", chunkConsumer);

      @SuppressWarnings("unchecked")
      ArgumentCaptor<List<Cookie>> captor = ArgumentCaptor.forClass(List.class);
      verify(chunkConsumer, times(2)).accept(captor.capture());
      List<List<Cookie>> allChunks = captor.getAllValues();

      List<Cookie> firstChunk = allChunks.getFirst();
      assertEquals(3, allChunks.getFirst().size());
      assertEquals("abc", firstChunk.get(0).getValue());
      assertEquals("def", firstChunk.get(1).getValue());
      assertEquals("ghi", firstChunk.get(2).getValue());

      List<Cookie> secondChunk = allChunks.get(1);
      assertEquals(1, secondChunk.size());
      assertEquals("xyz", secondChunk.getFirst().getValue());
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  void testParseParallel_twoChunksSameSize() {
    @SuppressWarnings("unchecked")
    Consumer<List<Cookie>> chunkConsumer = mock(Consumer.class);
    try (MockedStatic<FileUtils> utilities = mockStatic(FileUtils.class)) {
      utilities.when(() -> FileUtils.processLines(anyString(), eq(true), any(FileUtils.LineProcessor.class)))
          .thenAnswer(invocation -> {
            FileUtils.LineProcessor processor = invocation.getArgument(2);
            processor.process("cookie1,2025-07-07T10:00:00Z");
            processor.process("cookie2,2025-07-07T11:00:00Z");
            processor.process("cookie3,2025-07-08T12:00:00Z");
            processor.process("cookie4,2025-07-08T13:00:00Z");
            processor.process("cookie5,2025-07-08T13:00:00Z");
            processor.process("cookie6,2025-07-08T13:00:00Z");
            return null;
          });

      target.parseParallel("file.csv", chunkConsumer);

      @SuppressWarnings("unchecked")
      ArgumentCaptor<List<Cookie>> captor = ArgumentCaptor.forClass(List.class);
      verify(chunkConsumer, times(2)).accept(captor.capture());
      List<List<Cookie>> allChunks = captor.getAllValues();
      assertEquals(2, allChunks.size());

      List<Cookie> firstChunk = allChunks.getFirst();
      assertEquals(3, allChunks.getFirst().size());
      assertEquals("cookie1", firstChunk.get(0).getValue());
      assertEquals("cookie2", firstChunk.get(1).getValue());
      assertEquals("cookie3", firstChunk.get(2).getValue());

      List<Cookie> secondChunk = allChunks.get(1);
      assertEquals(3, secondChunk.size());
      assertEquals("cookie4", secondChunk.get(0).getValue());
      assertEquals("cookie5", secondChunk.get(1).getValue());
      assertEquals("cookie6", secondChunk.get(2).getValue());
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  void testParseParallel_emptyFile() {
    @SuppressWarnings("unchecked")
    Consumer<List<Cookie>> chunkConsumer = mock(Consumer.class);
    try (MockedStatic<FileUtils> utilities = mockStatic(FileUtils.class)) {
      utilities.when(() -> FileUtils.processLines(anyString(), eq(true), any(FileUtils.LineProcessor.class)))
          .thenAnswer(_ -> null);

      target.parseParallel("file.csv", chunkConsumer);

      verify(chunkConsumer, times(0)).accept(any());
    } catch (Exception e) {
      fail(e);
    }
  }
}