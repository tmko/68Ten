package com.ten68.marketing.webfront.rest;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ten68.marketing.webfront.WebfrontApplication.AUDIT_MARKER;

@Slf4j
@RestController
@RequestMapping
public class RestRequestChatEcho {
    private static final String REST_CHAT = "/v1/api/chat";
    private static final String REST_ECHO = "/v1/api/echo";

    @Autowired
    LogicChatEcho logicChatEcho;

    @PostMapping(REST_CHAT)
    public ResponseEntity<String> forwardRequest (HttpSession session, @RequestBody String body) {
        log.info(AUDIT_MARKER, body);
        ResponseEntity<String> result = logicChatEcho.forwardRequest(session, body).singleObjectJsonRespond();
        log.info(AUDIT_MARKER, result.getBody());
        return result;
    }

    @PostMapping(REST_ECHO)
    public ResponseEntity<String> echoRequest (@RequestBody String input) {
        log.info(AUDIT_MARKER, input);
        return StructResponse.Echo(input).singleObjectJsonRespond();
    }

}
