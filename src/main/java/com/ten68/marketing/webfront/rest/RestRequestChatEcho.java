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
@RequestMapping("/v1/api")
public class RestRequestChatEcho {

    @Autowired
    LogicChatEcho logicChatEcho;

    @PostMapping("/chat")
    public ResponseEntity<String> forwardRequest (HttpSession session, @RequestBody String body) {
        try {
            return logicChatEcho.forwardRequest(session, body).oneLineJsonRespond();
        } catch (Exception e) {
            return (new StructResponse(e)).oneLineJsonRespond();
        }
    }

    @PostMapping("/echo")
    public ResponseEntity<String> echoRequest (@RequestBody String input) {
        return (new StructResponse(HttpStatus.OK, "Echo", input)).oneLineJsonRespond();
    }

}
