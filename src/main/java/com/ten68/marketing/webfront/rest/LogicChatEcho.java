package com.ten68.marketing.webfront.rest;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
public class LogicChatEcho {
    private static final String SESSION_HTTP_CLIENT = "SESSION_HTTP_CLIENT";

    @Value("${spring.forward.url}")
    private String forwardUrl;

    @Value("${spring.forward.port}")
    private int forwardPort;


    private HttpClient getHttpClientFromSession (HttpSession session) {
        HttpClient httpClient = (HttpClient)session.getAttribute(SESSION_HTTP_CLIENT);
        boolean newClientNeeded = httpClient == null || httpClient.isTerminated();

        if (newClientNeeded) {
            httpClient = HttpClient.newHttpClient();
            session.setAttribute(SESSION_HTTP_CLIENT, httpClient);
        }
        return httpClient;
    }


    public ResponseEntity<String> forwardRequest(HttpSession session, @RequestBody String body) throws IOException, InterruptedException {
        final URI uri = URI.create("%s:%s".formatted(forwardUrl, forwardPort));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = getHttpClientFromSession(session);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return HttpStatus.OK.value() != response.statusCode() ?
            simpleJsonRespond(HttpStatus.OK, "Content", response.body()) :
            simpleJsonRespond(HttpStatus.INTERNAL_SERVER_ERROR, "Error", response.body());
    }


    public static ResponseEntity<String> simpleJsonRespond(HttpStatus status, String key, String value) {
        String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
        String simpleSingleJSON = "{\"%s\":\"%s\"}".formatted(key,encodedValue);
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(simpleSingleJSON);
    }

    public static ResponseEntity<String> simpleJsonRespond(Exception e) {
        return simpleJsonRespond(HttpStatus.INTERNAL_SERVER_ERROR, "Error", e.getMessage());
    }


}
