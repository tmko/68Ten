package com.ten68.marketing.webfront.rest;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RestRequestChatEcho {
    private static final String REST_CHAT = "/v1/api/chat";
    private static final String REST_ECHO = "/v1/api/echo";

    @Autowired
    LogicChatEcho logicChatEcho;

    @PostMapping(REST_CHAT)
    public ResponseEntity<String> forwardRequest (HttpSession session, @RequestBody String body) {
        return logicChatEcho.forwardRequest(session, body).singleObjectJsonRespond();
    }

    @PostMapping(REST_ECHO)
    public ResponseEntity<String> echoRequest (@RequestBody String input) {
        return (new StructResponse(HttpStatus.OK, "Echo", input)).singleObjectJsonRespond();
    }

}
