package com.ten68.marketing.webfront.socket;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Slf4j
@Component
public class LogicTailAuditLog {

    @Value("${spring.auditLog.filepath}")
    private String tailingAuditLog;

    public StructMessage process(StructMessage message) throws Exception {

        try (ReversedLinesFileReader reader = ReversedLinesFileReader.builder()
                .setPath(Paths.get(tailingAuditLog))
                .setBufferSize(4096)
                .setCharset(StandardCharsets.UTF_8)
                .get()
        ) {

            //reader.forEach(line -> System.out.println(line));
        }


        return new StructMessage(System.currentTimeMillis(), "something here " + System.currentTimeMillis());


    }



}
