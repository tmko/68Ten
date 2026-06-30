package com.ten68.marketing.webfront.socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LogicTailAuditLogTest {

    @TempDir
    Path tempDir;

    private LogicTailAuditLog logicTailAuditLog;
    private Path logFile;

    @BeforeEach
    void setUp() throws IOException {
        logFile = tempDir.resolve("audit.log");
        logicTailAuditLog = new LogicTailAuditLog();
        ReflectionTestUtils.setField(logicTailAuditLog, "tailingAuditLog", logFile.toString());
    }

    @Test
    void process_returnsMessagesWithTimestampGreaterThanInput() throws Exception {
        Files.writeString(logFile, """
                100 message1
                200 message2
                300 message3
                400 message4
                """);
        StructMessage input = new StructMessage(200L, "input");
        List<StructMessage> result = logicTailAuditLog.process(input);
        assertThat(result).hasSize(2);

        assertThat(result.get(0).timestamp()).isEqualTo(300L);
        assertThat(result.get(0).message()).isEqualTo("message3");
        assertThat(result.get(1).timestamp()).isEqualTo(400L);
        assertThat(result.get(1).message()).isEqualTo("message4");

    }

    @Test
    void process_returnsEmptyListWhenNoNewerMessages() throws Exception {
        Files.writeString(logFile, """
                100 message1
                200 message2
                """);
        StructMessage input = new StructMessage(200L, "input");
        List<StructMessage> result = logicTailAuditLog.process(input);
        assertThat(result).isEmpty();
    }

    @Test
    void process_filtersInvalidLines() throws Exception {
        Files.writeString(logFile, """
                100 valid
                invalid line without timestamp
                200 also valid
                """);
        StructMessage input = new StructMessage(50L, "input");
        List<StructMessage> result = logicTailAuditLog.process(input);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).timestamp()).isEqualTo(100L);
        assertThat(result.get(1).timestamp()).isEqualTo(200L);
    }

    @Test
    void process_returnsResultsInChronologicalOrder() throws Exception {
        Files.writeString(logFile, """
                100 first
                200 second
                300 third
                """);
        StructMessage input = new StructMessage(0L, "input");
        List<StructMessage> result = logicTailAuditLog.process(input);
        assertThat(result).hasSize(3);
        assertThat(result.get(0).timestamp()).isEqualTo(100L);
        assertThat(result.get(1).timestamp()).isEqualTo(200L);
        assertThat(result.get(2).timestamp()).isEqualTo(300L);
    }

    @Test
    void process_readsUpToMaxLookBackLines() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            sb.append(i * 10).append(" line").append(i).append("\n");
        }
        Files.writeString(logFile, sb.toString());
        StructMessage input = new StructMessage(-1L, "input");
        List<StructMessage> result = logicTailAuditLog.process(input);
        assertThat(result).hasSize(30);
    }

    @Test
    void process_emptyFileReturnsEmptyList() throws Exception {
        Files.writeString(logFile, "");
        StructMessage input = new StructMessage(0L, "input");
        List<StructMessage> result = logicTailAuditLog.process(input);
        assertThat(result).isEmpty();
    }

    @Test
    void process_fileWithOnlyInvalidLines_returnsEmptyList() throws Exception {
        Files.writeString(logFile, """
                not a log line
                also not valid
                """);
        StructMessage input = new StructMessage(0L, "input");
        List<StructMessage> result = logicTailAuditLog.process(input);
        assertThat(result).isEmpty();
    }
}
