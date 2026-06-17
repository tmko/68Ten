package com.ten68.marketing.webfront.component;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileWebSocketHandler extends TextWebSocketHandler {

    @Value("${spring.listening.filepath}")
    private String filepath;

    private String tailFile () {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
/*                if (currentLineNumber >= lineToRead) {
                    content.append(line).append(System.lineSeparator());
                    successfullyReadLastLine = currentLineNumber;
                }
                if (currentLineNumber >= maxTargetLine) {
                    break;
                }
                currentLineNumber++;*/
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }



    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        long time = System.currentTimeMillis();
        session.sendMessage(new TextMessage("sadfasdfasdfasdfasfasfasfasd" + filepath + time));
        session.getAttributes().put(CookieLogPositionInterceptor.LAST_LOG_TIMESTAMP_COOKIE, time);
    }
}
