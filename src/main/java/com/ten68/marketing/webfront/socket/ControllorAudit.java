package com.ten68.marketing.webfront.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ControllorAudit {

    /*
        /v1/app/audit - The endpoint where browser send message to server
        /topic/auditEvents -the endpoint where server stack message for browser
     */
    @MessageMapping("audit")
    @SendTo("/topic/auditEvents")
    public StructMessage process(StructMessage message) throws Exception {
        return new StructMessage(System.currentTimeMillis(), message.message());
    }

}
