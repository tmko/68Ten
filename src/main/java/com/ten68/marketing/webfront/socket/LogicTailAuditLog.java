package com.ten68.marketing.webfront.socket;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class LogicTailAuditLog {
    private static final int MAX_LOOK_BACK = 30;

    @Value("${spring.auditLog.filepath}")
    private String tailingAuditLog;

    public List<StructMessage> process(StructMessage message) throws Exception {
        List<String> latestLogs = new ArrayList<>();

        try (ReversedLinesFileReader reader = ReversedLinesFileReader.builder()
                .setPath(Paths.get(tailingAuditLog))
                .setBufferSize(4096)
                .setCharset(StandardCharsets.UTF_8)
                .get()
        ) {

            for (int i=0; i<MAX_LOOK_BACK; i++) {
                String line = reader.readLine();
                if ( line == null )
                    break;
                latestLogs.add(line);
            }

            return latestLogs.stream()
                    .map(StructMessage::build)
                    .filter(x -> !StructMessage.INVALID.equals(x))
                    .filter(x -> x.isLater(message))
                    .sorted()
                    .toList();

        }
    }




}
