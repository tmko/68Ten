package com.ten68.marketing.webfront.rest;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ten68.marketing.webfront.rest.LogicChatEcho.simpleJsonRespond;

@RestController
@RequestMapping("/v1/api")
public class RestRequestChatEcho {

    @Autowired
    LogicChatEcho logicChatEcho;

    @PostMapping("/chat")
    public ResponseEntity<String> forwardRequest (
            HttpSession session,
            @RequestBody String body
    ) {
        try {
            return logicChatEcho.forwardRequest(session, body);
        } catch (Exception e) {
            return simpleJsonRespond(e);
        }
    }

    @PostMapping("/echo")
    public ResponseEntity<String> echoRequest (
            @RequestBody String input
    ) {
        return simpleJsonRespond(HttpStatus.OK, "Echo", input);
    }

}
