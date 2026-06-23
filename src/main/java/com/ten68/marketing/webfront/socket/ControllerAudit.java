package com.ten68.marketing.webfront.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import org.apache.commons.io.input.ReversedLinesFileReader;


//@Controller
public class ControllerAudit {

    //@MessageMapping(WebSocketAudit.SOCKET_PUB_ENDPOINT)
    //@SendTo(WebSocketAudit.SOCKET_SUB_TOPIC)
    public StructMessage process(StructMessage message) throws Exception {


        /*
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return new StructMessage(System.currentTimeMillis(), "something here " + System.currentTimeMillis());


    }



}
